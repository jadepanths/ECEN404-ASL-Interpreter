import json
import os
import shutil

# File paths
json_file = '../WLASL_v0.3.json'
gloss_list_file = '../lists/test20gloss.txt'

# Directories
videos_dir = '../videos'
signs_dir = '../test/20gloss/vid'

# Read the JSON file
with open(json_file, 'r') as f:
    data = json.load(f)

# Create a dictionary mapping glosses to their video IDs
gloss_video_map = {}
for gloss_data in data:
    gloss = gloss_data['gloss']
    video_ids = [instance['video_id'] for instance in gloss_data['instances']]
    gloss_video_map[gloss] = video_ids

# Read the text file containing the list of 100 glosses
with open(gloss_list_file, 'r') as f:
    gloss_list = [line.strip() for line in f.readlines()]

# Create the "signs" directory and subdirectories for each gloss
os.makedirs(signs_dir, exist_ok=True)
for gloss in gloss_list:
    os.makedirs(os.path.join(signs_dir, gloss), exist_ok=True)

# Copy the video files from the "videos" directory to the appropriate subdirectories
for gloss in gloss_list:
    video_ids = gloss_video_map[gloss]
    for video_id in video_ids:
        video_file = f'{video_id}.mp4'
        source_path = os.path.join(videos_dir, video_file)
        dest_path = os.path.join(signs_dir, gloss, video_file)
        shutil.copy(source_path, dest_path)
