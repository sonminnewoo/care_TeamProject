package kr.spring.care.board.controller;

import kr.spring.care.board.model.Board;
import kr.spring.care.board.model.Comment;
import kr.spring.care.board.model.CommentResponse;
import kr.spring.care.board.service.CommentService;
import kr.spring.care.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    
    @GetMapping("/list/{num}")
    public ResponseEntity<CommentResponse> listComments(@PathVariable long num) {
        List<Comment> commentList = commentService.list(num);
        long count = commentService.count(num);

        CommentResponse response = new CommentResponse(commentList, count);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 댓글 추가
    @PostMapping("create")
    public ResponseEntity<String> createComment(@RequestBody Comment comment) {
        commentService.insert(comment);
        return new ResponseEntity<String>("Comment added successfully", HttpStatus.OK);
    }
    
    // 댓글 수정
    @PutMapping("update")
    public ResponseEntity<String> updateComment(@RequestBody Comment comment){
    	commentService.update(comment);
    	return new ResponseEntity<>("Comment updated successfully", HttpStatus.OK); //응답상태와 문자열을 반환
    }

    // 댓글 삭제
    @DeleteMapping("/delete/{cnum}")
    public ResponseEntity<String> deleteComment(@PathVariable("cnum") long cnum) {
        commentService.delete(cnum);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }
    
    
    
    
}