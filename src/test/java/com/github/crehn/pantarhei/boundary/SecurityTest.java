package com.github.crehn.pantarhei.boundary;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.crehn.pantarhei.api.Query;
import com.github.crehn.pantarhei.api.QuerySyntaxException;

@RunWith(MockitoJUnitRunner.class)
public class SecurityTest extends AbstractSipBoundaryTest {

    @Test(expected = QuerySyntaxException.class)
    public void shouldNotInjectSingleQuotes() {
        boundary.querySips(new Query("-'" + TAG1));
    }

    @Test(expected = QuerySyntaxException.class)
    public void shouldNotInjectDoubleQuotes() {
        boundary.querySips(new Query("-\"" + TAG1));
    }

    // TODO: prevent XSS
}
