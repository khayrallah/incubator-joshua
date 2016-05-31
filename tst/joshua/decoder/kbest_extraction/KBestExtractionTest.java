/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
 package joshua.decoder.kbest_extraction;

import static com.google.common.base.Charsets.UTF_8;
import static java.nio.file.Files.readAllBytes;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import joshua.decoder.Decoder;
import joshua.decoder.JoshuaConfiguration;
import joshua.decoder.Translation;
import joshua.decoder.segment_file.Sentence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Reimplements the kbest extraction regression test
 * TODO (fhieber): this test strangely only works with StateMinimizing KenLM.
 * This is to be investigated
 */
public class KBestExtractionTest {
  
  private static final String CONFIG = "resources/kbest_extraction/joshua.config";
  private static final String INPUT = "a b c d e";
  private static final Path GOLD_PATH = Paths.get("resources/kbest_extraction/output.scores.gold");
  
  private JoshuaConfiguration joshuaConfig = null;
  private Decoder decoder = null;
  
  @Before
  public void setUp() throws Exception {
    joshuaConfig = new JoshuaConfiguration();
    joshuaConfig.readConfigFile(CONFIG);
    joshuaConfig.outputFormat = "%i ||| %s ||| %c";
    decoder = new Decoder(joshuaConfig, "");
  }
  
  @After
  public void tearDown() throws Exception {
    decoder.cleanUp();
    decoder = null;
  }
  
  @Test
  public void givenInput_whenKbestExtraction_thenOutputIsAsExpected() throws IOException {
    final String translation = decode(INPUT).toString();
    final String gold = new String(readAllBytes(GOLD_PATH), UTF_8);
    assertEquals(gold, translation);
  }
  
  private Translation decode(String input) {
    final Sentence sentence = new Sentence(input, 0, joshuaConfig);
    return decoder.decode(sentence);
  }

}