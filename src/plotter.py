import matplotlib.pyplot as plt

# plotter.py
# Author: Taylor Heimbichner
# A simple python script for plotting the output of EigenenergyFinder.java
# Requires Matplotlib

def makePNG(filename, energy, potential):
    file = open(filename)

    xs = file.readline().strip().split()
    xs = list(map(float, xs))

    ys = file.readline().strip().split()
    ys = list(map(float, ys))

    plt.plot(xs, ys)
    plt.ylabel("ψ ($\AA^{-1/2}$)")
    plt.xlabel("x ($\AA$)")
    plt.title("Wavefunction associated with E=" + energy + " for V=" + potential)
    # plt.ylim(-1, 1)
    plt.savefig(filename + ".png")
    plt.clf()

makePNG("harmonic 0.txt", "3.380695eV", "3$eV/\AA^2x^2$");
makePNG("harmonic 1.txt", "10.14244eV", "3$eV/\AA^2x^2$");
makePNG("harmonic 2.txt", "16.90392eV", "3$eV/\AA^2x^2$");
makePNG("harmonic 3.txt", "23.66585eV", "3$eV/\AA^2x^2$");
makePNG("linear 0.txt",   "1.591175eV", "1$eV/\AA|x|$");
makePNG("linear 1.txt",   "3.651801eV", "1$eV/\AA|x|$");
makePNG("linear 2.txt",   "5.073221eV", "1$eV/\AA|x|$");
makePNG("linear 3.txt",   "6.384832eV", "1$eV/\AA|x|$");
makePNG("cubic 0.txt",    "4.344862eV", "5$eV/\AA^3|x|^3$");
makePNG("cubic 1.txt",    "14.65646eV", "5$eV/\AA^3|x|^3$");
makePNG("cubic 2.txt",    "27.05777eV", "5$eV/\AA^3|x|^3$");
makePNG("cubic 3.txt",    "40.44586eV", "5$eV/\AA^3|x|^3$");
makePNG("quartic 0.txt",  "4.422897eV", "5$eV/\AA^4x^4$");
makePNG("quartic 1.txt",  "15.84949eV", "5$eV/\AA^4x^4$");
makePNG("quartic 2.txt",  "31.09916eV", "5$eV/\AA^4x^4$");
makePNG("quartic 3.txt",  "48.57378eV", "5$eV/\AA^4x^4$");
