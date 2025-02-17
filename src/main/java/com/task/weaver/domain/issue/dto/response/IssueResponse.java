package com.task.weaver.domain.issue.dto.response;

import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.task.dto.response.ResponseUpdateDetail;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueResponse {
	private UUID projectId;
	private UUID taskId;
	private String taskTitle;
	private UUID issueId;
	private String issueTitle;
	private String issueContent;
	private String status;
	private UUID assigneeId;
	private String assigneeNickname;
	private URL assigneeProfileImage;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private ResponseUpdateDetail lastUpdateDetail;

	public IssueResponse(Issue issue) {
		this.projectId = issue.getTask().getProject().getProjectId();
		this.taskId = issue.getTask().getTaskId();
		this.taskTitle = issue.getTask().getTaskTitle();
		this.issueId = issue.getIssueId();
		this.issueTitle = issue.getIssueTitle();
		this.issueContent = issue.getIssueContent();
		this.status = issue.getStatus().toString();
		this.assigneeId = issue.getAssignee().getId();
		this.assigneeNickname = issue.getAssignee().resolveMemberByLoginType().getNickname();
		this.assigneeProfileImage = issue.getAssignee().resolveMemberByLoginType().getProfileImage();
		this.startDate = issue.getStartDate();
		this.endDate = issue.getEndDate();
		this.lastUpdateDetail = ResponseUpdateDetail.builder()
				.memberUuid(issue.getModifier().getId())
				.userNickname(issue.getModifier().resolveMemberByLoginType().getNickname())
				.updatedDate(issue.getModDate())
				.build();
	}
}