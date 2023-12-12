package com.ifellowtask;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;


public class FileSorter {
    private static final long memory = (long) (Runtime.getRuntime().freeMemory() * 0.8);

    public static void fileSorter(File dataFile) {
        File outputFile = new File("data_sorted.txt");
        List<Path> chunkPaths = new ArrayList<>();
        int chunkNum = 0;

        try (LineIterator lineIterator = FileUtils.lineIterator(dataFile, "UTF-8")) {
            String line = null;
            if (lineIterator.hasNext()) {
                line = lineIterator.nextLine();
            }
            while (line != null) {
                long lineSizeInByte = 8 + line.length() * 2L;
                List<String> chunk = new ArrayList<>();
                long currentChunkSize = 0;
                while (currentChunkSize + lineSizeInByte <= memory && line != null) {
                    currentChunkSize += lineSizeInByte;
                    chunk.add(line);
                    if (lineIterator.hasNext()) {
                        line = lineIterator.nextLine();
                        lineSizeInByte = 8 + line.length() * 2L;
                    } else {
                        line = null;
                    }
                }
                chunk.sort(Comparator.comparingLong(s -> Long.parseLong(s.split(":")[0])));
                Path chunkPath = Path.of("chunk_" + chunkNum + ".txt");
                chunkPaths.add(chunkPath);
                Files.write(chunkPath, chunk);
                chunkNum++;
            }

            PriorityQueue<LineReader> queue = new PriorityQueue<>();
            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(outputFile.toPath()))) {
                for (Path chunkPath : chunkPaths) {
                    queue.add(new LineReader(chunkPath));
                }
                while (!queue.isEmpty()) {
                    LineReader reader = queue.poll();
                    writer.println(reader.getValue() + ":" + reader.getString());
                    if (reader.next()) {
                        queue.offer(reader);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            chunkPaths.forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                }
            });
            try {
                String name = dataFile.getName();
                Files.delete(dataFile.toPath());
                outputFile.renameTo(new File(name));
            } catch (IOException e) {
            }
        }
    }



    private static class LineReader implements Comparable<LineReader> {

        private final BufferedReader reader;
        private Long value;
        private String string;

        public LineReader(Path filePath) throws IOException {
            reader = Files.newBufferedReader(filePath);
            next();
        }

        public String getString() {
            return string;
        }

        public Long getValue() {
            return value;
        }

        public boolean next() throws IOException {
            String str;
            if ((str = reader.readLine()) != null) {
                String[] toPars = str.split(":");
                value = Long.parseLong(toPars[0]);
                string = toPars[1];
                return true;
            } else {
                reader.close();
                return false;
            }
        }

        @Override
        public int compareTo(LineReader other) {
            return value.compareTo(other.getValue());
        }
    }
}
