package com.Ghibli.GhibliAPI.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TextToImageRequest {

    @JsonProperty("text_prompts")
    private List<TextPrompt> text_prompt;

    private double cfg_scale = 7;

    private int height;
    private int width;
    private int samples = 1;

    private String style_preset;

    private int steps = 30;

    // Inner class for text prompt
    public static class TextPrompt {
        private String text;

        public TextPrompt(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    // Constructor with dynamic width and height
    public TextToImageRequest(String text, String style, int width, int height) {
        this.text_prompt = List.of(new TextPrompt(text));
        this.style_preset = style;
        this.width = width;
        this.height = height;
    }

    // Getters for JSON serialization
    public List<TextPrompt> getText_prompt() {
        return text_prompt;
    }

    public double getCfg_scale() {
        return cfg_scale;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getSamples() {
        return samples;
    }

    public String getStyle_preset() {
        return style_preset;
    }

    public int getSteps() {
        return steps;
    }
}
