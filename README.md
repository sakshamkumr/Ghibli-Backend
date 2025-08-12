# Ghibli Image Generator - Backend

This is the **backend** API for the Ghibli Image Generator project.  
It provides endpoints to generate Ghibli-style images from text or uploaded images.

## Features

- REST API endpoints to generate images:
  - `/api/v1/generate` (image + prompt)
  - `/api/v1/generate-from-text` (text prompt)
- Built with Spring Boot
- Handles image processing and external API integration
- Secure API key usage with environment variables

## Technologies Used

- Java 21, Spring Boot 3.5
- Maven build system
- Lombok
- Thumbnailator (for image manipulation)
- Spring Cloud OpenFeign (if used)
- Stability AI 

## Live Demo

[Live App](https://ghibli-image-generator-production.up.railway.app/)

## [Frontend Source Code](https://github.com/sakshamkumr/Ghibli-Image-Generator)

### Prerequisites

- Java 21 SDK
- Maven
- Thumnailator

### Installation

```bash
git clone https://github.com/yourusername/ghibli-backend.git
cd ghibli-backend
mvn clean install
