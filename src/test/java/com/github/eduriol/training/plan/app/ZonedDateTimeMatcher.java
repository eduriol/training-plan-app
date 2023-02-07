package com.github.eduriol.training.plan.app;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class ZonedDateTimeMatcher extends BaseMatcher<ZonedDateTime> {

    private final ZonedDateTime expected;
    private final long tolerance;
    private final ChronoUnit unit;

    public ZonedDateTimeMatcher(ZonedDateTime expected, long tolerance, ChronoUnit unit) {
        this.expected = expected;
        this.tolerance = tolerance;
        this.unit = unit;
    }

    @Override
    public boolean matches(Object actual) {
        ZonedDateTime actualZonedDateTime = ZonedDateTime.parse(actual.toString());
        return Math.abs(expected.until(actualZonedDateTime, unit)) <= tolerance;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a ZonedDateTime within " + tolerance + " " + unit + " of " + expected);
    }
}
