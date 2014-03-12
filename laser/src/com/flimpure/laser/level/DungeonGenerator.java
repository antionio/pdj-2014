package com.flimpure.laser.level;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;

public class DungeonGenerator {

	private static final Random random = new Random();

	private static float[][] generateSmoothNoise(float[][] baseNoise, int octave) {
		int width = baseNoise.length;
		int height = baseNoise[0].length;

		float[][] smoothNoise = new float[width][height];

		int samplePeriod = 1 << octave; // calculates 2 ^ k
		float sampleFrequency = 1.0f / samplePeriod;

		for (int i = 0; i < width; i++) {
			// calculate the horizontal sampling indices
			int sample_i0 = (i / samplePeriod) * samplePeriod;
			int sample_i1 = (sample_i0 + samplePeriod) % width; // wrap around
			float horizontal_blend = (i - sample_i0) * sampleFrequency;

			for (int j = 0; j < height; j++) {
				// calculate the vertical sampling indices
				int sample_j0 = (j / samplePeriod) * samplePeriod;
				int sample_j1 = (sample_j0 + samplePeriod) % height; // wrap
																		// around
				float vertical_blend = (j - sample_j0) * sampleFrequency;

				// blend the top two corners
				float top = interpolate(baseNoise[sample_i0][sample_j0], baseNoise[sample_i1][sample_j0],
						horizontal_blend);

				// blend the bottom two corners
				float bottom = interpolate(baseNoise[sample_i0][sample_j1], baseNoise[sample_i1][sample_j1],
						horizontal_blend);

				// final blend
				smoothNoise[i][j] = interpolate(top, bottom, vertical_blend);
			}
		}

		return smoothNoise;
	}

	private static float[][] generatePerlinNoise(float[][] baseNoise, int octaveCount, float persistance) {
		int width = baseNoise.length;
		int height = baseNoise[0].length;

		float[][][] smoothNoise = new float[octaveCount][][]; // an array of 2D
																// arrays
																// containing

		// generate smooth noise
		for (int i = 0; i < octaveCount; i++) {
			smoothNoise[i] = generateSmoothNoise(baseNoise, i);
		}

		float[][] perlinNoise = new float[width][height];
		float amplitude = 1.0f;
		float totalAmplitude = 0.0f;

		// blend noise together
		for (int octave = octaveCount - 1; octave >= 0; octave--) {
			amplitude *= persistance;
			totalAmplitude += amplitude;

			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
				}
			}
		}

		// normalisation
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				perlinNoise[i][j] /= totalAmplitude;
			}
		}

		return perlinNoise;
	}

	private static float interpolate(float x0, float x1, float alpha) {
		return x0 * (1 - alpha) + alpha * x1;
	}

	public static int[][] getTileMap(int width, int height) {
		float baseNoise[][] = new float[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				baseNoise[x][y] = random.nextInt(2);
			}
		}
		float perlin[][] = generatePerlinNoise(baseNoise, 2, 0.2f);
		int[][] tileMap = new int[width][height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				tileMap[x][y] = (int) Math.floor(perlin[x][y] * 10);
			}
		}

        // normalize map
        int w = tileMap.length;
        int h = tileMap[0].length;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (tileMap[x][y] < 4) {
                    tileMap[x][y] = 0;
                } else {
                    tileMap[x][y] = 5;
                }
            }
        }

		return tileMap;
	}

	public static BufferedImage getImageFromMap(int[][] map) {
		int w = map.length;
		int h = map[0].length;
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		int[] pixels = new int[w * h];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int i = x + y * w;
				// land
				if (map[x][y] == 0)
					pixels[i] = 0x616161;
				if (map[x][y] == 1)
					pixels[i] = 0x616161;
				if (map[x][y] == 2)
					pixels[i] = 0x616161;
				if (map[x][y] == 3)
					pixels[i] = 0x616161;
//				if (map[x][y] == 4)
//					pixels[i] = 0x639C18;
//				if (map[x][y] == 5)
//					pixels[i] = 0x7BC618;
//				if (map[x][y] == 6)
//					pixels[i] = 0x94D639;
//				// edge of water
//				if (map[x][y] == 7)
//					pixels[i] = 0x087B7B;
//				if (map[x][y] == 8)
//					pixels[i] = 0x006363;
//				if (map[x][y] == 9)
//					pixels[i] = 0x004A4A;
//				if (map[x][y] == 10)
//					pixels[i] = 0x003139;
			}
		}
		img.setRGB(0, 0, w, h, pixels, 0, w);
		return img;
	}

	public static Pixmap getPixmapFromMap(int[][] map) {
		int w = map.length;
		int h = map[0].length;
		Pixmap img = new Pixmap(w, h, Format.RGB888);
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				// land
				if (map[x][y] == 0) {
					img.setColor(new Color(0.4f, 0.4f, 0.4f, 1));
					img.drawPixel(x, y);
				}
				if (map[x][y] == 1) {
					img.setColor(new Color(0.7f, 0.6f, 0.1f, 1));
					img.drawPixel(x, y);
				}
				if (map[x][y] == 2) {
					img.setColor(new Color(0.2f, 0.3f, 0.05f, 1));
					img.drawPixel(x, y);
				}
				if (map[x][y] == 3) {
					img.setColor(new Color(0.25f, 0.35f, 0.05f, 1));
					img.drawPixel(x, y);
				}
				if (map[x][y] == 4) {
					img.setColor(new Color(0.4f, 0.6f, 0.05f, 1));
					img.drawPixel(x, y);
				}
				if (map[x][y] == 5) {
					img.setColor(new Color(0.45f, 0.7f, 0.05f, 1));
					img.drawPixel(x, y);
				}
				if (map[x][y] == 6) {
					img.setColor(new Color(0.5f, 0.75f, 0.05f, 1));
					img.drawPixel(x, y);
				}
				// edge of water
				if (map[x][y] == 7) {
					img.setColor(new Color(0.1f, 0.5f, 0.5f, 1));
					img.drawPixel(x, y);
				}
				if (map[x][y] == 8) {
					img.setColor(new Color(0.1f, 0.4f, 0.4f, 1));
					img.drawPixel(x, y);
				}
				if (map[x][y] == 9) {
					img.setColor(new Color(0.1f, 0.3f, 0.3f, 1));
					img.drawPixel(x, y);
				}
				if (map[x][y] == 10) {
					img.setColor(new Color(0.1f, 0.2f, 0.2f, 1));
					img.drawPixel(x, y);
				}
			}
		}

		return img;
	}

	public static void main(String[] args) {
		while (true) {
			int w = 96;
			int h = 48;

			int[][] map = DungeonGenerator.getTileMap(w, h);

			JOptionPane.showMessageDialog(
					null,
					null,
					"LumberCraft Dungeon Generator",
					JOptionPane.YES_NO_OPTION,
					new ImageIcon(DungeonGenerator.getImageFromMap(map).getScaledInstance(w * 3, h * 3,
							Image.SCALE_AREA_AVERAGING)));
		}
	}
}
