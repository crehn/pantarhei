package com.github.crehn.pantarhei.api;

import java.util.Iterator;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Grammar:
 *
 * <pre>
 * <code>
 * QUERY ::= { INCLUDING_TAG | XCLUDING_TAG }
 * INCLUDING_TAG ::= '+' + TAGNAME
 * EXCLUDING_TAG ::= '-' + TAGNAME
 * TAGNAME ::= { ?Letter? | ?Number? | '-' }
 * </code>
 * </pre>
 */
@Slf4j
@Value
public class Query {
    private static final String JPQL_QUERY = "SELECT s from SipEntity s WHERE ";

    @RequiredArgsConstructor
    private static class Tokenizer implements Iterable<Token> { // NOPMD
        private int index = 0; // NOPMD

        @NonNull
        private String queryString;

        @Override
        public Iterator<Token> iterator() { // NOPMD
            return new Iterator<Token>() {
                @Override
                public boolean hasNext() {
                    return index < queryString.length();
                }

                @Override
                public Token next() {
                    StringBuilder result = new StringBuilder();
                    for (; index < queryString.length(); index++) {
                        char c = queryString.charAt(index);
                        if (Character.isWhitespace(c))
                            index++;
                        else if (Character.isLetter(c) || Character.isDigit(c) || c == '+' || c == '-')
                            result.append(c);
                        else
                            throw new QuerySyntaxException("Illegal character [" + c + "] at position " + index);

                        if (isDelimiter(c) && result.length() > 0)
                            return toToken(result.toString());
                    }
                    return toToken(result.toString());
                }

                private boolean isDelimiter(char c) {
                    return Character.isWhitespace(c) || c == '(' || c == ')';
                }

                private Token toToken(String string) {
                    switch (string.charAt(0)) {
                    case '+':
                        return new Token(TokenType.includingTag, string.substring(1));
                    case '-':
                        return new Token(TokenType.excludingTag, string.substring(1));
                    }
                    throw new QuerySyntaxException("Expected '+' or '-' but found [" + string + "] at " + index);

                }
            };
        }
    }

    @Value
    private static class Token {
        TokenType type;
        String value;

        public String toJpql() {
            return type.toJpql(value);
        }
    }

    @AllArgsConstructor
    private enum TokenType {
        includingTag("'%s' MEMBER OF s.tags"), //
        excludingTag("'%s' NOT MEMBER OF s.tags");

        String template;

        public String toJpql(String value) {
            return String.format(template, value);
        }
    }

    String queryString;

    @Override
    public String toString() {
        return queryString;
    }

    public String toJpql() {
        StringBuilder result = new StringBuilder(JPQL_QUERY);
        Tokenizer tokenizer = new Tokenizer(queryString.trim());
        boolean firstToken = true;
        for (Token token : tokenizer) {
            log.debug("found token [{}]", token);
            if (!firstToken)
                result.append(" AND ");
            result.append(token.toJpql());
            firstToken = false;
        }
        return result.toString();
    }
}
