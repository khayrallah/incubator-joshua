cd ..
java  -classpath $CLASS_PATH:./bin -Xmx2000m -Xms2000m joshua.decoder.Decoder example/example.config.srilm example/example.test.in example/example.nbest.srilm.out
