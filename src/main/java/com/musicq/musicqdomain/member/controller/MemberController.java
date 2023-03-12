package com.musicq.musicqdomain.member.controller;

import com.musicq.musicqdomain.member.domain.Member;
import com.musicq.musicqdomain.member.domain.MemberImage;
import com.musicq.musicqdomain.member.dto.MemberInfoResDto;
import com.musicq.musicqdomain.member.persistence.MemberImageRepository;
import com.musicq.musicqdomain.member.persistence.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/members")
public class MemberController {
    private final MemberRepository memberRepository;
    private final MemberImageRepository memberImageRepository;

    // JSON 으로 반환하기 위해 ResponseEntity<Object> 로 반환하고 count 라는 key 값을 갖도록 Map 을 만들어서 반환
    @GetMapping("/id/{id}")
    public ResponseEntity<Object> checkId(
            @Valid @PathVariable("id") String id
    ){
        Map<String, Long> response = new HashMap<>();
        response.put("count", memberRepository.countById(id));
        return ResponseEntity.ok(response);
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<Object> checkEmail(
            @Valid @PathVariable("email") String email
    ){
        Map<String, Long> response = new HashMap<>();
        response.put("count", memberRepository.countByEmail(email));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<Object> checkNickname(
            @Valid @PathVariable("nickname") String nickname
    ){
        Map<String, Long> response = new HashMap<>();
        response.put("count", memberRepository.countByNickname(nickname));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberInfoResDto> signup(
            @Valid @RequestBody String memberInfo
    ){
        Map<String, Object> entityMap = memberRepository.memberInfoToEntity(memberInfo);
        Member member = (Member) entityMap.get("member");
        MemberImage memberImage = (MemberImage) entityMap.get("member_image");

        memberRepository.save(member);
        memberImageRepository.save(memberImage);

        MemberInfoResDto response = memberRepository.entityToMemberInfoRes(member, memberImage);
        return ResponseEntity.ok(response);
    }
}
