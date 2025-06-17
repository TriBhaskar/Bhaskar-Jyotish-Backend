# Bhaskar Jyotish Project Documentation

## Overview

**Bhaskar Jyotish** is a web-based platform that offers personalized Vedic astrology services through professional consultations, customized content subscriptions, and interactive tools. The platform is designed for individuals seeking traditional Jyotish insights delivered through modern technology.

---

## Objectives

- Enable users to book consultations with certified astrologers.
- Offer paid subscription plans for customized astrological insights.
- Provide detailed birth charts and astrological data.
- Create a community around Jyotish knowledge and services.
- Monetize through subscriptions, consultations, and a digital marketplace.

---

## Core Modules & Features

### 1. Astrologer Consultation Platform

#### a. Astrologer Profiles
- Name, photo, and biography
- Certifications and qualifications
- Areas of specialization (e.g., career, relationships, health)
- Years of experience
- Client ratings and reviews
- Consultation fee per session
- Availability calendar

#### b. Appointment Management
- User interface for selecting time slots
- Sync with astrologer's calendar
- Email/SMS notifications for booking, reminders, and changes
- Options for rescheduling or canceling
- Booking history

#### c. Video Consultation
- Integrated video conferencing (e.g., Jitsi, Zoom SDK)
- Real-time chat during the session
- Screen sharing for chart explanation
- Consent-based session recording

#### d. Payments & Transactions
- Secure payment gateway integration (e.g., Razorpay, Stripe)
- Multiple payment options (UPI, Cards, Wallets)
- Automated receipts and invoices
- Commission model for astrologers

#### e. Feedback & Review
- Star rating system
- Detailed reviews
- Reporting system for inappropriate behavior
- Admin dashboard for quality control

---

### 2. Subscription-Based Personalized Content

#### a. Subscription Tiers
- One Month
- Three Months
- Six Months
- Annual Plan

#### b. Content Types
- Daily, weekly, monthly horoscopes
- Career & finance insights
- Relationship compatibility analysis
- Health guidance
- Muhurta (auspicious timing) suggestions
- Transit predictions
- Dasha period interpretation

#### c. Content Delivery
- Web dashboard
- Email alerts
- SMS notifications (optional for critical events)
- PDF downloadable reports

---

### 3. Birth Chart & Astrological Tools

- Birth chart generation using third-party APIs (e.g., Swiss Ephemeris)
- Form validation (birth date, time, place)
- Interactive visual representation of:
    - Rashi (Moon sign)
    - Lagna (Ascendant)
    - Planetary positions & aspects
    - Nakshatra (Birth star)
    - Dashas and planetary transits

---

### 4. Additional Features

#### a. Event Alerts
- Transit alerts
- Personalized astrological reminders
- Favorable/Unfavorable day notifications

#### b. Knowledge Center
- Articles & blogs on Jyotish principles
- Video tutorials (YouTube integration)
- Glossary of astrological terms

#### c. Community Forum
- Discussion boards by topic
- Moderated threads
- Ask-an-Astrologer section

#### d. Digital Marketplace
- Remedial products (gemstones, yantras)
- Books & spiritual guides
- Affiliate and sponsored listings

#### e. Mobile Compatibility
- Fully responsive UI/UX
- PWA (Progressive Web App) support
- Scope for future Android/iOS native app

---

## Functional Requirements

### User Roles

- **Admin**
    - Manage astrologers
    - View earnings and subscriptions
    - Moderate content and forum
- **Astrologer**
    - Manage profile and availability
    - Join consultations
    - View bookings and earnings
- **Customer**
    - Book consultations
    - Subscribe to plans
    - Access content and tools

### Key Use Cases

| Use Case                        | Actor        | Description                                          |
|-------------------------------|--------------|------------------------------------------------------|
| Book Consultation              | Customer     | Schedule and pay for a session with an astrologer   |
| View Horoscope                 | Customer     | Receive personalized daily/weekly predictions       |
| Update Availability            | Astrologer   | Set available time slots                            |
| Submit Feedback                | Customer     | Review astrologers post-consultation                |
| Manage Content Plan            | Admin        | Add or edit subscription content                    |
| Purchase Marketplace Product   | Customer     | Buy suggested remedial items                        |

---

## Technical Stack (Proposed)

| Layer        | Technology               |
|--------------|--------------------------|
| Frontend     | Angular / React          |
| Backend      | Spring Boot / Node.js    |
| Database     | PostgreSQL / MongoDB     |
| Video        | Jitsi / Zoom SDK         |
| Payments     | Razorpay / Stripe        |
| Hosting      | AWS / DigitalOcean       |
| Auth         | JWT / OAuth2             |
| API          | REST (OpenAPI spec)      |

---

## Future Enhancements

- AI-powered astrological predictions
- Voice-based consultation booking (Alexa/Google Assistant)
- Native Android/iOS apps
- Multi-language support (Hindi, English, etc.)
- Loyalty points and referral system

---

## Questions / Clarifications

## Astrologer Onboarding Process

- **Self-Registration**: Astrologers can register themselves on the platform
- **Credential Validation**: Internal team validates credentials through a structured interview process
- **Service Authorization**: Only approved astrologers are permitted to offer services on the platform

## User Birth Chart Access

- **Conditional Data Sharing**: Kundli/chart data is shared with astrologers only after booking confirmation
- **Pre-Consultation Review**: Astrologers can review generated charts before the consultation begins
- **Optional Upload Feature**: Users may upload existing kundli PDFs as an optional feature

## Consultation Structure

- **Predefined Durations**: Sessions are available in standard durations (30 minutes, 1 hour)
- **Astrologer Selection**: Astrologers can select their available slot durations from predefined options
- **Variable Pricing**: Fee structure varies based on session length

## Legal Requirements

### Privacy Policy
Outlines storage and protection of user data including:
- Date of Birth (DOB)
- Location information
- Payment information

### Terms of Service
Platform rules covering:
- Payment procedures
- Cancellation policies
- Refund processes
- Guidelines for both customers and astrologers

### Consent Disclaimer
Users must acknowledge that astrology services are provided for guidance purposes only

### Recording Consent
Explicit opt-in required from both parties if consultation sessions are to be recorded

## Content Update Schedule

| Content Type | Recommended Frequency |
|--------------|----------------------|
| Daily Horoscope | Daily (morning) |
| Weekly Overview | Every Sunday |
| Monthly Forecast | 1st of each month |
| Transit Events | Upon planetary transit |
| Dasha Updates | At dasha/sub-dasha start |
| Special Muhurtas & Reminders | As per astrological calendar |

---

*This document serves as a reference for the astrology platform's operational guidelines and requirements.*

---

## Conclusion

The Bhaskar Jyotish platform aims to be a comprehensive and scalable solution for astrology consultations, horoscopic content delivery, and spiritual products. With its blend of tradition and technology, the platform will serve users looking for authentic Jyotish services in a modern interface.
