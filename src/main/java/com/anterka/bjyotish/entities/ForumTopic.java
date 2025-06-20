package com.anterka.bjyotish.entities;

// ==============================================
// FORUM TOPIC ENTITY
// ==============================================

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "forum_topics")
public class ForumTopic implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "forum_topic_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "forum_topic_sequence_generator", sequenceName = "seq_forum_topic_id", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private ForumCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bjyotish_user_id", nullable = false)
    private BjyotishUser bjyotishUser;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_pinned")
    @Builder.Default
    private Boolean isPinned = false;

    @Column(name = "is_locked")
    @Builder.Default
    private Boolean isLocked = false;

    @Column(name = "views_count")
    @Builder.Default
    private Integer viewsCount = 0;

    @Column(name = "replies_count")
    @Builder.Default
    private Integer repliesCount = 0;

    @Column(name = "last_reply_at")
    private Instant lastReplyAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_reply_by")
    private BjyotishUser lastReplyBy;

    @Column(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    @Builder.Default
    private Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ForumReply> forumReplies;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Utility methods
    public boolean isPinned() {
        return isPinned != null && isPinned;
    }

    public boolean isLocked() {
        return isLocked != null && isLocked;
    }

    public void pin() {
        this.isPinned = true;
    }

    public void unpin() {
        this.isPinned = false;
    }

    public void lock() {
        this.isLocked = true;
    }

    public void unlock() {
        this.isLocked = false;
    }

    public void incrementViews() {
        this.viewsCount = (this.viewsCount != null ? this.viewsCount : 0) + 1;
    }

    public void updateLastReply(BjyotishUser replyBy) {
        this.lastReplyAt = Instant.now();
        this.lastReplyBy = replyBy;
        this.repliesCount = (this.repliesCount != null ? this.repliesCount : 0) + 1;
    }

    public void decrementRepliesCount() {
        this.repliesCount = Math.max(0, (this.repliesCount != null ? this.repliesCount : 0) - 1);
    }

    public boolean hasReplies() {
        return repliesCount != null && repliesCount > 0;
    }

    public boolean hasRecentActivity() {
        if (lastReplyAt == null) return false;
        return lastReplyAt.isAfter(Instant.now().minusSeconds(86400)); // Last 24 hours
    }

    public String getAuthorName() {
        return bjyotishUser != null ? bjyotishUser.getFirstName() : "Unknown User";
    }

    public String getLastReplyByName() {
        return lastReplyBy != null ? lastReplyBy.getFirstName() : "Unknown User";
    }

    public String getCategoryName() {
        return category != null ? category.getName() : "Unknown Category";
    }

    public String getContentPreview(int maxLength) {
        if (content == null || content.trim().isEmpty()) {
            return "No content available";
        }

        String cleanContent = content.replaceAll("<[^>]*>", "").trim(); // Remove HTML tags
        return cleanContent.length() > maxLength ? cleanContent.substring(0, maxLength) + "..." : cleanContent;
    }

    public String getTopicSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(title);
        if (isPinned()) summary.append(" [PINNED]");
        if (isLocked()) summary.append(" [LOCKED]");
        summary.append(" - ").append(repliesCount).append(" replies, ").append(viewsCount).append(" views");
        return summary.toString();
    }

    public String getActivityStatus() {
        if (isLocked()) return "Locked";
        if (hasRecentActivity()) return "Active";
        if (hasReplies()) return "Replied";
        return "No replies";
    }

    public boolean canReply() {
        return !isLocked();
    }

    public int getContentWordCount() {
        if (content == null || content.trim().isEmpty()) {
            return 0;
        }
        return content.trim().split("\\s+").length;
    }

    public List<ForumReply> getApprovedReplies() {
        if (forumReplies == null) return List.of();
        return forumReplies.stream()
                .filter(ForumReply::isApproved)
                .toList();
    }
}
