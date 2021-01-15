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
import sys
import shutil

filepath = '/mydata/soundbinaryclassifier_project/data/'+str(sys.argv[1])+'/'+str(sys.argv[2])
os.mkdir(filepath+'/train')

for folder in os.listdir(filepath):
    os.mkdir(filepath+'/train/'+folder)
    counter = 0
    for filename in os.listdir(filepath+'/'+folder):
        if (filename.endswith(".wav")):
            print(filename)
            plt.interactive(False)
            clip, sample_rate = librosa.load(filepath+'/'+folder+'/'+filename, sr=None)
            fig = plt.figure(figsize=[0.9,0.9])
            ax = fig.add_subplot(111)
            ax.axes.get_xaxis().set_visible(False)
            ax.axes.get_yaxis().set_visible(False)
            ax.set_frame_on(False)
            S = librosa.feature.melspectrogram(y=clip, sr=sample_rate)
            librosa.display.specshow(librosa.power_to_db(S, ref=np.max))
            name  = filepath+'/train/'+folder+'/'+folder+'_'+str(counter)+'_spectrogram.jpg'
            plt.savefig(name, dpi=400, bbox_inches='tight',pad_inches=0)
            plt.close()    
            fig.clf()
            plt.close(fig)
            plt.close('all')
            counter += 1
            continue
        else:
            continue

shutil.rmtree(filepath+'/train/train')


# dimensions of our images.
img_width, img_height = 150, 150

train_data_dir = filepath+'/train'
#validation_data_dir = 'data/validation'
nb_train_samples = 200
#nb_validation_samples = 50
epochs = 20
batch_size = 20

if K.image_data_format() == 'channels_first':
    input_shape = (3, img_width, img_height)
else:
    input_shape = (img_width, img_height, 3)


model = Sequential()
model.add(Conv2D(32, (3, 3), input_shape=input_shape))
model.add(Activation('relu'))
model.add(MaxPooling2D(pool_size=(2, 2)))

model.add(Conv2D(32, (3, 3)))
model.add(Activation('relu'))
model.add(MaxPooling2D(pool_size=(2, 2)))

model.add(Conv2D(64, (3, 3)))
model.add(Activation('relu'))
model.add(MaxPooling2D(pool_size=(2, 2)))

model.add(Flatten())
model.add(Dense(64))
model.add(Activation('relu'))
model.add(Dropout(0.5))
model.add(Dense(1))
model.add(Activation('sigmoid'))

model.compile(loss='binary_crossentropy',
              optimizer='rmsprop',
              metrics=['accuracy'])

# only rescaling
train_datagen = ImageDataGenerator(rescale=1. / 255)
#test_datagen = ImageDataGenerator(rescale=1. / 255)

train_generator = train_datagen.flow_from_directory(
    train_data_dir,
    target_size=(img_width, img_height),
    batch_size=batch_size,
    class_mode='binary')

#validation_generator = test_datagen.flow_from_directory(
#    validation_data_dir,
#    target_size=(img_width, img_height),
#    batch_size=batch_size,
#    class_mode='binary')

model.fit(
    train_generator,
    steps_per_epoch=nb_train_samples // batch_size,
    epochs=epochs#,
    #validation_data=validation_generator,
    #validation_steps=nb_validation_samples // batch_size
    )

model.save('/mydata/soundbinaryclassifier_project/data/'+str(sys.argv[1])+'/'+str(sys.argv[2])+".h5")

print("Saved model to disk")