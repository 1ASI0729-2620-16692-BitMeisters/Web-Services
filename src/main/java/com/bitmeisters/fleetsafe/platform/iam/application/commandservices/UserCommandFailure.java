package com.bitmeisters.fleetsafe.platform.iam.application.commandservices;

public sealed interface UserCommandFailure
        permits UserCommandFailure.EmailAlreadyRegistered,
                UserCommandFailure.NotFound,
                UserCommandFailure.AlreadyInactive {

    String messageKey();

    record EmailAlreadyRegistered() implements UserCommandFailure {
        @Override
        public String messageKey() {
            return "iam.user.error.email-already-registered";
        }
    }

    record NotFound() implements UserCommandFailure {
        @Override
        public String messageKey() {
            return "iam.user.error.not-found";
        }
    }

    record AlreadyInactive() implements UserCommandFailure {
        @Override
        public String messageKey() {
            return "iam.user.error.already-inactive";
        }
    }
}
