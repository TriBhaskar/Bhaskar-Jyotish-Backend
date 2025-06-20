package com.anterka.bjyotish.entities;

// ==============================================
// FORUM REPLY ENTITY
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
@Table(name = "forum_replies")
public class ForumReply implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "forum_reply_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "forum_reply_sequence_generator", sequenceName = "seq_forum_reply_id", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private ForumTopic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bjyotish_user_id", nullable = false)
    private BjyotishUser bjyotishUser;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_reply_id")
    private ForumReply parentReply;

    @Column(name = "is_approved")
    @Builder.Default
    private Boolean isApproved = true;

    @Column(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    @Builder.Default
    private Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "parentReply", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ForumReply> childReplies;

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
    public boolean isApproved() {
        return isApproved != null && isApproved;
    }

    public void approve() {
        this.isApproved = true;
    }

    public void reject() {
        this.isApproved = false;
    }

    public boolean isTopLevelReply() {
        return parentReply == null;
    }

    public boolean isNestedReply() {
        return parentReply != null;
    }

    public boolean hasChildReplies() {
        return childReplies != null && !childReplies.isEmpty();
    }

    public int getChildRepliesCount() {
        return childReplies != null ? childReplies.size() : 0;
    }

    public String getAuthorName() {
        return bjyotishUser != null ? bjyotishUser.getFirstName() : "Unknown User";
    }

    public String getTopicTitle() {
        return topic != null ? topic.getTitle() : "Unknown Topic";
    }

    public String getParentReplyAuthor() {
        return parentReply != null && parentReply.getBjyotishUser() != null ?
                parentReply.getBjyotishUser().getFirstName() : null;
    }

    public String getContentPreview(int maxLength) {
        if (content == null || content.trim().isEmpty()) {
            return "No content available";
        }

        String cleanContent = content.replaceAll("<[^>]*>", "").trim(); // Remove HTML tags
        return cleanContent.length() > maxLength ? cleanContent.substring(0, maxLength) + "..." : cleanContent;
    }

    public String getReplySummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Reply by ").append(getAuthorName());
        if (isNestedReply()) {
            summary.append(" (replying to ").append(getParentReplyAuthor()).append(")");
        }
        summary.append(" - ").append(isApproved() ? "Approved" : "Pending");
        return summary.toString();
    }

    public boolean isRecentReply() {
        return createdAt != null && createdAt.isAfter(Instant.now().minusSeconds(86400)); // Last 24 hours
    }

    public int getContentWordCount() {
        if (content == null || content.trim().isEmpty()) {
            return 0;
        }
        return content.trim().split("\\s+").length;
    }

    public int getNestedLevel() {
        int level = 0;
        ForumReply current = this.parentReply;
        while (current != null) {
            level++;
            current = current.getParentReply();
        }
        return level;
    }

    public List<ForumReply> getApprovedChildReplies() {
        if (childReplies == null) return List.of();
        return childReplies.stream()
                .filter(ForumReply::isApproved)
                .toList();
    }

    public String getStatusDescription() {
        if (!isApproved()) return "Pending Approval";
        if (isRecentReply()) return "Recent Reply";
        if (hasChildReplies()) return "Has " + getChildRepliesCount() + " replies";
        return "Reply";
    }

    public boolean canHaveReplies() {
        return isApproved() && topic != null && topic.canReply();
    }
}
