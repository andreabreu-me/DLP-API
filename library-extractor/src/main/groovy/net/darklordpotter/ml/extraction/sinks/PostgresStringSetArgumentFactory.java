/*
 * Copyright (C) 2013 Michael Rose
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 *  of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.darklordpotter.ml.extraction.sinks;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.Argument;
import org.skife.jdbi.v2.tweak.ArgumentFactory;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

public class PostgresStringSetArgumentFactory implements ArgumentFactory<Set<String>> {
    public boolean accepts(Class<?> expectedType, Object value, StatementContext ctx) {
        return value instanceof Set;
    }

    public Argument build(Class<?> expectedType, final Set<String> value, StatementContext ctx) {
        return new Argument() {
            public void apply(int position, PreparedStatement statement, StatementContext ctx) throws SQLException {
                // in postgres no need to (and in fact cannot) free arrays
                Array ary = ctx.getConnection().createArrayOf("text", value.toArray());
                statement.setArray(position, ary);
            }
        };
    }
}