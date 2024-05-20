import os
import json
import csv
import shutil

WLASL_100 = "wlasl_100.txt"
json_file = "../WLASL_v0.3.json"
signs = "signs"
video_path = "../videos"

list_100 = []
content = json.load(open('../WLASL_v0.3.json'))

with open(WLASL_100, 'r') as fd:
    reader = csv.reader(fd)
    for row in reader:
        list_100.append(row[0])

for element in list_100:
    for entry in content:
        gloss = entry['gloss']
        # print("gloss:", gloss)

        # element == gloss (change for testing)
        if "hello" == gloss:
            # Creating the directory if not exists
            sign_path = signs + "/" + element
            isExist = os.path.exists(sign_path)
            if not isExist:
                os.mkdir(sign_path)
                print("New directory is added")

            # check for videos in the instance
            for instance in entry['instances']:
                video_id = instance['video_id']
                # check and copy video in videos into sings' respective folder
                # sign_path video_path

                source_video_path = os.path.join(video_path, f"{video_id}.mp4")
                dest_video_path = os.path.join(sign_path, f"{video_id}.mp4")

                if not os.path.exists(dest_video_path):
                    shutil.copyfile(source_video_path, dest_video_path)
