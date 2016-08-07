package com.github.crehn.pantarhei.api;

import java.util.Iterator;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Value
public class Query {
    private static final String JPQL_QUERY = "SELECT s from SipEntity s WHERE ";

    @RequiredArgsConstructor
    private static class Tokenizer implements Iterable<Token> {
        protected static final String WHITESPACE = " \r\n\t";
        protected static final String DELIMITERS = WHITESPACE + "()";
        private int index = 0;

        @NonNull
        private String queryString;

        @Override
        public Iterator<Token> iterator() {
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
                        if (isWhiteSpace(c))
                            index++;
                        else
                            result.append(c);

                        if (isDelimiter(c) && result.length() > 0)
                            return toToken(result.toString());
                    }
                    return toToken(result.toString());
                }

                private boolean isWhiteSpace(char c) {
                    return WHITESPACE.contains("" + c);
                }

                private boolean isDelimiter(char c) {
                    return DELIMITERS.contains("" + c);
                }

                private Token toToken(String string) {
                    return new Token(TokenType.includingTag, string.substring(1));
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

    private enum TokenType {
        includingTag;

        public String toJpql(String value) {
            return "'" + value + "' MEMBER OF s.tags";
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
