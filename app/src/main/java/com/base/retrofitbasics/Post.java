package com.base.retrofitbasics;

public class Post {
    public class Content {
        private String rendered;

        public Content(String rendered) {
            this.rendered = rendered;
        }

        public String getRendered() {
            return this.rendered;
        }
    }

    private Content content;

    public Post(Content content) {
        this.content = content;
    }

    public Content getContent() {
        return this.content;
    }
}




