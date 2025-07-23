# Bhaskar Jyotish Development Roadmap

## Current Status ✅
- [x] Project setup with database & Redis
- [x] Frontend project initialized
- [x] Customer authentication system (login, registration, email verification, forgot password)

---

## Phase 1: Core User Management (Current Priority) 🎯

### 1.1 Astrologer Registration System (IMMEDIATE NEXT STEP)
**Recommendation: Create separate API endpoints for astrologer registration**

**Why separate APIs?**
- Different validation requirements
- Different data collection needs
- Different approval workflows
- Cleaner separation of concerns

**Tasks:**
- [ ] Create `POST /api/auth/astrologer/register` endpoint
- [ ] Implement astrologer-specific validation
    - Experience validation
    - Specialization selection
    - Certification upload handling
    - Language preference collection
- [ ] Create astrologer profile creation flow
- [ ] Implement approval workflow (status: PENDING_VERIFICATION)
- [ ] Admin notification system for new astrologer registrations

**Estimated Time:** 3-4 days

### 1.2 User Profile Management
- [ ] Customer profile update API
- [ ] Birth details collection and management
- [ ] Address management system
- [ ] Profile image upload functionality

**Estimated Time:** 2-3 days

### 1.3 Admin User Management
- [ ] Admin login system
- [ ] Astrologer approval/rejection workflow
- [ ] User status management (suspend/activate)
- [ ] Basic admin dashboard APIs

**Estimated Time:** 2-3 days

---

## Phase 2: Astrologer Management System

### 2.1 Astrologer Profile Enhancement
- [ ] Astrologer profile completion
- [ ] Specialization management
- [ ] Certification verification system
- [ ] Portfolio/experience showcase

### 2.2 Availability Management
- [ ] Astrologer availability CRUD operations
- [ ] Time slot management
- [ ] Blocked dates/times system
- [ ] Timezone handling

### 2.3 Astrologer Dashboard APIs
- [ ] Dashboard statistics
- [ ] Earnings overview
- [ ] Consultation history
- [ ] Profile analytics

**Estimated Time:** 5-6 days

---

## Phase 3: Birth Chart & Astrological Data

### 3.1 Birth Chart Generation
- [ ] Integrate Swiss Ephemeris or similar library
- [ ] Birth chart calculation APIs
- [ ] Planetary position calculations
- [ ] Chart data storage in JSONB format

### 3.2 Astrological Tools
- [ ] Rashi (Moon sign) calculation
- [ ] Lagna (Ascendant) determination
- [ ] Nakshatra calculation
- [ ] Basic compatibility checks

**Estimated Time:** 7-8 days

---

## Phase 4: Consultation System (Core Business Logic)

### 4.1 Consultation Booking
- [ ] Available slot calculation algorithm
- [ ] Booking creation and validation
- [ ] Conflict prevention system
- [ ] Booking confirmation workflow

### 4.2 Consultation Management
- [ ] Consultation status management
- [ ] Rescheduling system
- [ ] Cancellation handling
- [ ] No-show management

### 4.3 Video Integration Preparation
- [ ] Meeting room creation APIs
- [ ] Video platform integration (Jitsi/Zoom)
- [ ] Session recording setup
- [ ] Chat functionality APIs

**Estimated Time:** 8-10 days

---

## Phase 5: Payment System

### 5.1 Payment Gateway Integration
- [ ] Razorpay/Stripe integration
- [ ] Payment processing APIs
- [ ] Transaction management
- [ ] Refund handling

### 5.2 Subscription System
- [ ] Subscription plan management
- [ ] User subscription APIs
- [ ] Recurring payment handling
- [ ] Subscription lifecycle management

**Estimated Time:** 6-7 days

---

## Phase 6: Content Management

### 6.1 Personalized Content System
- [ ] Content generation APIs
- [ ] Content delivery system
- [ ] Content scheduling
- [ ] Email/SMS integration

### 6.2 Knowledge Center
- [ ] Blog system implementation
- [ ] Article management
- [ ] Content categorization
- [ ] SEO optimization

**Estimated Time:** 5-6 days

---

## Phase 7: Review & Rating System

### 7.1 Review Management
- [ ] Review submission APIs
- [ ] Rating calculation system
- [ ] Review moderation
- [ ] Review display system

**Estimated Time:** 3-4 days

---

## Phase 8: Notification System

### 8.1 Notification Infrastructure
- [ ] Notification creation and management
- [ ] Email notification system
- [ ] SMS integration
- [ ] Push notification setup

**Estimated Time:** 4-5 days

---

## Phase 9: Community Features

### 9.1 Forum System
- [ ] Forum categories and topics
- [ ] Reply system
- [ ] Moderation tools
- [ ] Community guidelines enforcement

**Estimated Time:** 6-7 days

---

## Phase 10: Digital Marketplace

### 10.1 Product Management
- [ ] Product catalog system
- [ ] Category management
- [ ] Inventory tracking
- [ ] Order processing

**Estimated Time:** 7-8 days

---

## Immediate Next Steps (Next 2 Weeks)

### Week 1: Astrologer Registration & Profile
1. **Day 1-2:** Implement astrologer registration API
2. **Day 3-4:** Create astrologer profile management
3. **Day 5:** Implement approval workflow
4. **Day 6-7:** Create basic admin approval system

### Week 2: User Profile & Availability
1. **Day 1-2:** Complete user profile management
2. **Day 3-4:** Implement astrologer availability system
3. **Day 5-7:** Create time slot management and blocked dates

---

## API Structure Recommendations

### Astrologer Registration
```
POST /api/auth/astrologer/register
{
  "email": "astrologer@example.com",
  "password": "password",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+91XXXXXXXXXX",
  "yearsOfExperience": 5,
  "specializations": ["CAREER", "RELATIONSHIPS"],
  "languagesSpoken": ["English", "Hindi"],
  "consultationFeePerHour": 500.00,
  "bio": "Experienced astrologer...",
  "certifications": ["file1.pdf", "file2.pdf"]
}
```

### Customer vs Astrologer Registration Differences
- **Customer:** Basic profile, birth details optional initially
- **Astrologer:** Extended profile, certifications required, approval needed
- **Validation:** Different rules for each user type
- **Workflow:** Customers activate immediately, astrologers need approval

---

## Development Best Practices

1. **API Versioning:** Use `/api/v1/` prefix
2. **Error Handling:** Consistent error response format
3. **Logging:** Comprehensive audit trail
4. **Testing:** Unit tests for each major feature
5. **Documentation:** OpenAPI/Swagger documentation
6. **Security:** JWT tokens, rate limiting, input validation

---

## Database Considerations

Your schema is comprehensive and well-structured. Key points:
- Use the existing `user_role` enum effectively
- Leverage the `astrologer_profiles` table for extended astrologer data
- Implement proper foreign key relationships
- Use the audit_logs table for tracking important changes

---

## Success Metrics

- [ ] Astrologers can register and get approved
- [ ] Users can book consultations
- [ ] Payment processing works end-to-end
- [ ] Basic admin functionality operational
- [ ] Core APIs documented and tested

**Total Estimated Timeline:** 12-14 weeks for MVP
**Immediate Focus:** Next 2-4 weeks on user management and astrologer systems