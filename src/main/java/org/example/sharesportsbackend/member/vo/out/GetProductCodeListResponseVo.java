package org.example.sharesportsbackend.member.vo.out;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetProductCodeListResponseVo {
    private List<String> productCodeList;
}