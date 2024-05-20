import cv2
import os
import random
import numpy as np
import concurrent.futures


def flip_video(video_path, output_path):
    video = cv2.VideoCapture(video_path)
    fps = int(video.get(cv2.CAP_PROP_FPS))
    width = int(video.get(cv2.CAP_PROP_FRAME_WIDTH))
    height = int(video.get(cv2.CAP_PROP_FRAME_HEIGHT))

    fourcc = cv2.VideoWriter_fourcc(*'mp4v')
    out = cv2.VideoWriter(output_path, fourcc, fps, (width, height))

    while video.isOpened():
        ret, frame = video.read()
        if not ret:
            break

        flipped_frame = cv2.flip(frame, 1)
        out.write(flipped_frame)

    video.release()
    out.release()


def rotate_video(video_path, output_path, angle):
    video = cv2.VideoCapture(video_path)
    fps = int(video.get(cv2.CAP_PROP_FPS))
    width = int(video.get(cv2.CAP_PROP_FRAME_WIDTH))
    height = int(video.get(cv2.CAP_PROP_FRAME_HEIGHT))

    fourcc = cv2.VideoWriter_fourcc(*'mp4v')
    out = cv2.VideoWriter(output_path, fourcc, fps, (width, height))

    while video.isOpened():
        ret, frame = video.read()
        if not ret:
            break

        rotation_matrix = cv2.getRotationMatrix2D((width / 2, height / 2), angle, 1)
        rotated_frame = cv2.warpAffine(frame, rotation_matrix, (width, height))
        out.write(rotated_frame)

    video.release()
    out.release()


def change_video_speed(video_path, output_path, speed_factor):
    video = cv2.VideoCapture(video_path)
    fps = int(video.get(cv2.CAP_PROP_FPS)) * speed_factor
    width = int(video.get(cv2.CAP_PROP_FRAME_WIDTH))
    height = int(video.get(cv2.CAP_PROP_FRAME_HEIGHT))

    fourcc = cv2.VideoWriter_fourcc(*'mp4v')
    out = cv2.VideoWriter(output_path, fourcc, fps, (width, height))

    while video.isOpened():
        ret, frame = video.read()
        if not ret:
            break

        out.write(frame)

    video.release()
    out.release()


def process_directory(subdir):
    print(f'Processing directory: {subdir}')

    video_files = [os.path.join(subdir, f) for f in os.listdir(subdir) if f.endswith('.mp4')]

    num_videos = len(video_files)

    # Randomly flip 50% of the videos
    flip_videos = random.sample(video_files, num_videos // 2)

    for video_file in flip_videos:
        output_path = os.path.join(subdir, f'flipped_{os.path.basename(video_file)}')
        flip_video(video_file, output_path)

    # Randomly rotate 50% of the remaining videos
    remaining_videos = list(set(video_files) - set(flip_videos))
    rotate_videos = random.sample(remaining_videos, len(remaining_videos) // 2)

    for video_file in rotate_videos:
        angle = random.uniform(-5, 5)
        output_path = os.path.join(subdir, f'rotated_{angle:.2f}_{os.path.basename(video_file)}')
        rotate_video(video_file, output_path, angle)

    # Randomly rotate 50% of the flipped videos
    rotate_flipped_videos = random.sample(flip_videos, len(flip_videos) // 2)

    for video_file in rotate_flipped_videos:
        input_path = os.path.join(subdir, f'flipped_{os.path.basename(video_file)}')
        angle = random.uniform(-5, 5)
        output_path = os.path.join(subdir, f'flipped_rotated_{angle:.2f}_{os.path.basename(video_file)}')
        rotate_video(input_path, output_path, angle)

    # Randomly change speed for 50% of all videos (including flipped, rotated, and flipped+rotated videos)
    speed_change_videos = random.sample(video_files, len(video_files) // 2)

    for video_file in speed_change_videos:
        speed_factor = random.uniform(0.7, 1.3)  # slow down (0.5x) or speed up (1.5x)
        if speed_factor == 1:
            speed_factor = 1.31
        output_path = os.path.join(subdir, f'speed_{speed_factor:.1f}_{os.path.basename(video_file)}')
        change_video_speed(video_file, output_path, speed_factor)

    print(f'Finished processing directory: {subdir}')


main_folder_dir = "data_augment"
# main_folder_dir = "raw_data/test_raw10_4"
subdirectories = [os.path.join(main_folder_dir, d) for d in os.listdir(main_folder_dir) if os.path.isdir(os.path.join(main_folder_dir, d))]


if __name__ == '__main__':
    # Process directories concurrently
    with concurrent.futures.ProcessPoolExecutor() as executor:
        executor.map(process_directory, subdirectories)

