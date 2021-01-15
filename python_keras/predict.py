import sys
from keras.preprocessing.image import ImageDataGenerator, array_to_img, img_to_array, load_img
import numpy as np
from keras import backend as K
from keras.models import load_model
import scipy
from PIL import Image
from keras.preprocessing.image import ImageDataGenerator, array_to_img, img_to_array, load_img
import matplotlib.pyplot as plt
from scipy import signal
from scipy.io import wavfile
import librosa
import librosa.display
import numpy as np
from keras.preprocessing.image import ImageDataGenerator
from keras.models import Sequential
from keras.layers import Conv2D, MaxPooling2D
from keras.layers import Activation, Dropout, Flatten, Dense
from keras import backend as K
import os

errorFile = open("errorFile.txt", "a")

try:
    model = load_model('/mydata/soundbinaryclassifier_project/data/'+str(sys.argv[1])+'/'+str(sys.argv[2])+'.h5')

    model.summary()

    img_width, img_height = 150, 150
    samples_to_predict = []
    expected = []

    if K.image_data_format() == 'channels_first':
        input_shape = (3, img_width, img_height)
    else:
        input_shape = (img_width, img_height, 3)

    plt.interactive(False)
    clip, sample_rate = librosa.load('/mydata/soundbinaryclassifier_project/data/'+str(sys.argv[1])+'/'+str(sys.argv[2])+"_predict.wav", sr=None)
    fig = plt.figure(figsize=[0.9,0.9])
    ax = fig.add_subplot(111)
    ax.axes.get_xaxis().set_visible(False)
    ax.axes.get_yaxis().set_visible(False)
    ax.set_frame_on(False)
    S = librosa.feature.melspectrogram(y=clip, sr=sample_rate)
    librosa.display.specshow(librosa.power_to_db(S, ref=np.max))
    name  = '/mydata/soundbinaryclassifier_project/data/'+str(sys.argv[1])+'/'+str(sys.argv[2])+'_spectrogram.jpg'
    plt.savefig(name, dpi=400, bbox_inches='tight',pad_inches=0)
    plt.close()    
    fig.clf()
    plt.close(fig)
    plt.close('all')
    img = Image.open('/mydata/soundbinaryclassifier_project/data/'+str(sys.argv[1])+'/'+str(sys.argv[2])+'_spectrogram.jpg')  # this is a PIL image
    img = img.resize((150, 150), Image.ANTIALIAS)
    x = img_to_array(img)  # this is a Numpy array with shape
    samples_to_predict.append(x)

    samples_to_predict = np.array(samples_to_predict)

    predictions = (model.predict(samples_to_predict) > 0.5).astype("int32")

    img.close()

    os.remove('/mydata/soundbinaryclassifier_project/data/'+str(sys.argv[1])+'/'+str(sys.argv[2])+'_spectrogram.jpg')

    print(str(predictions))

    if predictions[0]==0:
        print(20)
        sys.exit(20)
    elif predictions[0]==1:
        print(10)
        sys.exit(10)

except Exception as e:
    errorFile.write(str(e))



