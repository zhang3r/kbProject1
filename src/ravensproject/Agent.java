package ravensproject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

public class Agent {
	public Agent() {
	}

	public int Solve(RavensProblem problem) throws IOException {
		System.out.println("Ravens Problem: " + problem.getName());
		HashMap<String, RavensFigure> Ravefigures = problem.getFigures();
		int answer = -1;

		// Algo 1.
		List<RavensFigure> row1 = new ArrayList<>();
		row1.add(Ravefigures.get("A"));
		row1.add(Ravefigures.get("B"));
		row1.add(Ravefigures.get("C"));
		long row1_pix = blackPixelCount(row1, true);

		List<RavensFigure> row2 = new ArrayList<>();
		row2.add(Ravefigures.get("D"));
		row2.add(Ravefigures.get("E"));
		row2.add(Ravefigures.get("F"));
		long row2_pix = blackPixelCount(row2, true);

		List<RavensFigure> rowAns = new ArrayList<>();
		rowAns.add(Ravefigures.get("G"));
		rowAns.add(Ravefigures.get("H"));
		long rowAns_pix = blackPixelCount(rowAns, true);

		long ansFigure_pix = Math.abs(2 * row2_pix - rowAns_pix - row1_pix);
		long error = (long) (0.014 * ansFigure_pix);
		System.out.println("error is: " + error);
		if (problem.getName().contains("D")) {
			System.out.println("ansFigure: " + ansFigure_pix);
			for (int i = 1; i <= 8; i++) {
				long temp_pix = blackPixel(Ravefigures.get(String.valueOf(i)));
				System.out.println("Ans " + i + ": " + temp_pix);
				System.out.println("difference is: "
						+ Math.abs(temp_pix - ansFigure_pix));
				if (Math.abs(temp_pix - ansFigure_pix) <= error) {
					System.out.println("Answer chosen: " + i);
					return i;
				}
			}
			// Algo 2. A.
		} else if (problem.getName().contains("E")) {
			long a = blackPixel(Ravefigures.get("A"));
			long b = blackPixel(Ravefigures.get("B"));
			long c = blackPixel(Ravefigures.get("C"));

			long g = blackPixel(Ravefigures.get("G"));
			long h = blackPixel(Ravefigures.get("H"));

			long xorABC = Math.abs(Math.abs(a - b) - c);

			long xorGH = Math.abs(g - h);
			// Algo 2. B.

			long orABC = Math.abs(a + b - c);
			long orGH = g + h;

			for (int i = 1; i <= 8; i++) {
				long temp_pix = blackPixel(Ravefigures.get(String.valueOf(i)));
				if (Math.abs(Math.abs(temp_pix - xorGH) - xorABC) <= error) {
					System.out.println("Answer chosen: " + i);
					return i;
				} else if (Math.abs(Math.abs(temp_pix - orGH) - orABC) <= error) {
					System.out.println("Answer chosen: " + i);
					return i;
				}
			}
		}

		// Algo 4. catch all
		long ans_pix = Math.abs(blackPixel(Ravefigures.get("G"))
				- blackPixel(Ravefigures.get("D"))
				+ blackPixel(Ravefigures.get("F")));
		long closest_pix = Long.MAX_VALUE;
		System.out.println("ans_pix: " + ans_pix);
		for (int i = 1; i <= 8; i++) {
			long diff = Math.abs(blackPixel(Ravefigures.get(String.valueOf(i)))
					- ans_pix);
			System.out.println("diff: " + diff);
			if (diff < closest_pix) {
				closest_pix = diff;
				answer = i;

			}
		}
		System.out.println("Answer chosen: " + answer);
		return answer;
	}

	private long blackPixel(RavensFigure thisFigure) throws IOException {
		BufferedImage figureImage = ImageIO.read(new File(thisFigure
				.getVisual()));
		long black = 0, total = figureImage.getWidth()
				* figureImage.getHeight();
		for (int i = 0; i < figureImage.getWidth(); i++) {
			for (int j = 0; j < figureImage.getHeight(); j++) {
				black += figureImage.getRGB(i, j) != -1 ? 1 : 0;
			}
		}
		return black;
	}

	private long blackPixelCount(List<RavensFigure> figures, boolean pos)
			throws IOException {
		long pixelCount = 0;
		for (RavensFigure figure : figures) {

			pixelCount += pos ? blackPixel(figure) : -1 * blackPixel(figure);
		}
		return pixelCount;
	}

}
