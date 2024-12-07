package com.example.oauthApp.dto;


    public class ErrorResponse {
        private String error;
        private Object message;

        public ErrorResponse(String error, Object message) {
            this.error = error;
            this.message = message;
        }

    }
