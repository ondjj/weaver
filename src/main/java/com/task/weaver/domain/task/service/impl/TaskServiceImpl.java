package com.task.weaver.domain.task.service.impl;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.common.exception.task.TaskNotFoundException;
import com.task.weaver.common.exception.member.UserNotFoundException;
import com.task.weaver.domain.authorization.entity.Member;
import com.task.weaver.domain.authorization.repository.MemberRepository;
import com.task.weaver.domain.member.UserOauthMember;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.task.dto.request.RequestCreateTask;
import com.task.weaver.domain.task.dto.request.RequestGetTaskPage;
import com.task.weaver.domain.task.dto.request.RequestUpdateTask;
import com.task.weaver.domain.task.dto.response.ResponseGetTask;
import com.task.weaver.domain.task.dto.response.ResponseGetTaskList;
import com.task.weaver.domain.task.dto.response.ResponseUpdateDetail;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.repository.TaskRepository;
import com.task.weaver.domain.task.service.TaskService;
import com.task.weaver.domain.member.user.entity.User;
import com.task.weaver.domain.member.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Function;


@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    @Override
    public ResponseGetTask getTask(UUID taskId) throws NotFoundException, AuthorizationException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(new Throwable(String.valueOf(taskId))));

        Member modifier = task.getModifier();
        UserOauthMember userOauthMember = modifier.resolveMemberByLoginType();
        ResponseGetTask responseTask = new ResponseGetTask(task);

        ResponseUpdateDetail responseUpdateDetail = ResponseUpdateDetail.builder()
                .memberUuid(modifier.getId())
                .userNickname(userOauthMember.getNickname())
                .updatedDate(task.getModDate())
                .build();

        responseTask.setLastUpdateDetail(responseUpdateDetail);

        return responseTask;
    }

    @Override
    public ResponsePageResult<ResponseGetTaskList, Task> getTasks(RequestGetTaskPage requestGetTaskPage) throws NotFoundException, AuthorizationException {
        log.info("Project ID ={}", requestGetTaskPage.getProjectId());
        Project project = projectRepository.findById(requestGetTaskPage.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND, ErrorCode.PROJECT_NOT_FOUND.getMessage()));

        Pageable pageable = requestGetTaskPage.getPageable(Sort.by("taskId").descending());
        Page<Task> taskPage = taskRepository.findByProject(project, pageable);

        Function<Task, ResponseGetTaskList> fn = ResponseGetTaskList::new;
        return new ResponsePageResult<>(taskPage, fn);
    }

    @Override
    public Page<Task> getTasks(String status, Pageable pageable) throws NotFoundException, AuthorizationException {
        return taskRepository.findByStatus(status, pageable);
    }

    @Override
    public UUID addTask(RequestCreateTask request) throws AuthorizationException {
        Member writer = memberRepository.findById(request.getWriterUuid())
                .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(request.getProjectId()))));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(request.getProjectId()))));
        log.info("writer id : " + request.getWriterUuid());
        log.info("project id : " + request.getProjectId());
        Task entity = request.toEntity(writer, project);
        Task save = taskRepository.save(entity);
        return save.getTaskId();
    }

    @Override
    public void deleteTask(Task task) throws NotFoundException, AuthorizationException {
        taskRepository.delete(task);
    }

    @Override
    public void deleteTask(UUID taskId) throws NotFoundException, AuthorizationException {
        taskRepository.deleteById(taskId);
    }

    @Override
    public ResponseGetTask updateTask(Task originalTask, Task newTask) throws NotFoundException, AuthorizationException {
        Task task = taskRepository.findById(originalTask.getTaskId()).get();
        task.updateTask(newTask);
        task = taskRepository.save(task);
        return new ResponseGetTask(task);
    }

    @Override
    public ResponseGetTask updateTask(UUID originalTaskId, Task newTask) throws NotFoundException, AuthorizationException {
        Task task = taskRepository.findById(originalTaskId)
                .orElseThrow(() -> new TaskNotFoundException(new Throwable(String.valueOf(originalTaskId))));

        task.updateTask(newTask);
        taskRepository.save(task);
        return new ResponseGetTask(task);
    }


    @Override
    public ResponseGetTask updateTask(UUID taskId, RequestUpdateTask requestUpdateTask) throws NotFoundException, AuthorizationException {
        Task findTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(new Throwable(String.valueOf(taskId))));

        Member updater = memberRepository.findById(requestUpdateTask.getUpdaterUuid())
                .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(requestUpdateTask.getUpdaterUuid()))));

        findTask.updateTask(requestUpdateTask, updater);
        taskRepository.save(findTask);
        return new ResponseGetTask(findTask);
    }

    @Override
    public ResponseGetTask updateTaskStatus(UUID taskId, String status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(ErrorCode.TASK_NOT_FOUND, ErrorCode.TASK_NOT_FOUND.getMessage()));
        task.setStatus(status);
        taskRepository.save(task);
        return new ResponseGetTask(task);
    }
}