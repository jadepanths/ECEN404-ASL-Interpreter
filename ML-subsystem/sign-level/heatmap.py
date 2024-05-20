import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.colors as mcolors
import numpy as np

data = {
    "Category": [
        "I_love_you", "I_me", "book", "brother", "busy", "cold_sick", "eat", "excuse_me", "father",
        "fine_welcome", "finish", "food", "go", "happy", "hello", "help", "his_her_its", "hospital",
        "how", "hungry", "hurt", "large", "leave", "mother", "my", "name", "need", "please", "sad",
        "see", "sister", "small", "sorry", "thank_you", "they", "tired", "want", "we", "what", "when"
    ],
    "F1-score": [
        0.67, 0.77, 0.91, 1.00, 0.98, 0.86, 0.60, 0.91, 0.94, 0.87, 0.96, 0.00, 0.95, 0.78, 0.94,
        0.99, 0.94, 0.97, 0.84, 0.98, 0.88, 0.87, 0.85, 0.94, 0.78, 0.85, 0.82, 0.63, 0.94, 0.66,
        0.98, 1.00, 0.68, 0.89, 0.95, 0.92, 0.98, 0.65, 0.88, 0.84
    ],
}

df = pd.DataFrame(data)
df_sorted = df.sort_values(by="F1-score", ascending=True)

fig, ax = plt.subplots(figsize=(10, 12))
y_labels = df_sorted["Category"].values

for i, (index, row) in enumerate(df_sorted.iterrows()):
    score = row["F1-score"]
    bar_color = mcolors.to_rgba("green", alpha=score)
    ax.fill_betweenx([i - 0.5, i + 0.5], 0, score, color=bar_color)
    if score > 0:
        ax.text(score / 2, i, f"{score:.2f}", color="black", fontsize=14, ha="center", va="center")
    elif score == 0:
        ax.text(0.04, i, f"{score:.2f}", color="black", fontsize=14, ha="center", va="center")

ax.set_yticks(range(len(y_labels)))
ax.set_yticklabels(y_labels, fontsize=14)
ax.set_ylim(-0.5, len(y_labels) - 0.5)
ax.set_xlim(0, 1)
ax.set_xlabel("F1-score", fontsize=14)
ax.set_ylabel("Category", fontsize=14)
ax.set_title("F1-score Heatmap-like Visualization", fontsize=14)
plt.show()