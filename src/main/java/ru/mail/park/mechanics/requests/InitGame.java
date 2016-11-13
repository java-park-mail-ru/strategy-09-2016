package ru.mail.park.mechanics.requests;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Created by Solovyev on 03/11/2016.
 */
public class InitGame {
    @SuppressWarnings("NullableProblems")
    public static final class Request {
        @NotNull
        private Long self;
        @NotNull
        private Map<Long, String> names;

        public Map<Long, String> getNames() {
            return names;
        }

        public void setNames(Map<Long, String> names) {
            this.names = names;
        }

        @NotNull
        public Long getSelf() {
            return self;
        }

        public void setSelf(@NotNull Long self) {
            this.self = self;
        }

    }

}
