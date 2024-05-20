import json

# Set the directory where the JSON file is located
json_dir = '../WLASL_v0.3.json'

# Read the JSON file
with open(json_dir) as f:
    data = json.load(f)

# Count the number of videos for each gloss
video_counts = {}
for gloss_data in data:
    gloss = gloss_data['gloss']
    video_count = len(gloss_data['instances'])
    if gloss in video_counts:
        video_counts[gloss] += video_count
    else:
        video_counts[gloss] = video_count

# Sort the glosses by the number of videos in descending order
sorted_glosses = sorted(video_counts.items(), key=lambda x: x[1], reverse=True)

# Set the directory where the text file will be saved
output_dir = 'sorted_gloss.txt'

# Write the result to a text file
with open(output_dir, 'w') as f:
    for gloss, count in sorted_glosses:
        f.write(f'{gloss} {count}\n')

