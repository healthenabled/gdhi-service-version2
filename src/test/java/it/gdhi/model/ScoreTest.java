package it.gdhi.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ScoreTest {
    @Test
    public void shouldNotBreakForNullScore() throws Exception {
        Score score = new Score(null);
        assertNull(score.convertToPhase());
    }

    @Test
    public void shouldConvertToPhase1() throws Exception {
        Score score = new Score(1d);
        assertEquals(Integer.valueOf(1), score.convertToPhase());
    }

    @Test
    public void shouldRound0ToPhase1() throws Exception {
        Score score = new Score(0d);
        assertEquals(Integer.valueOf(1), score.convertToPhase());
    }

    @Test
    public void shouldRound0_1ToPhase1() throws Exception {
        Score score = new Score(0.1d);
        assertEquals(Integer.valueOf(1), score.convertToPhase());
    }

    @Test
    public void shouldRound1_01ToPhase1() throws Exception {
        Score score = new Score(1.01d);
        assertEquals(Integer.valueOf(1), score.convertToPhase());
    }

    @Test
    public void shouldRound1_05ToPhase2() throws Exception {
        Score score = new Score(1.05d);
        assertEquals(Integer.valueOf(2), score.convertToPhase());
    }

    @Test
    public void shouldRound1_1ToPhase2() throws Exception {
        Score score = new Score(1.1d);
        assertEquals(Integer.valueOf(2), score.convertToPhase());
    }

    @Test
    public void shouldRound1_5ToPhase2() throws Exception {
        Score score = new Score(1.5d);
        assertEquals(Integer.valueOf(2), score.convertToPhase());
    }

    @Test
    public void shouldRound3_5ToPhase4() throws Exception {
        Score score = new Score(3.5d);
        assertEquals(Integer.valueOf(4), score.convertToPhase());
    }

}