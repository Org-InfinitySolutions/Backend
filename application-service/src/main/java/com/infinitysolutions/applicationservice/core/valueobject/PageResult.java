package com.infinitysolutions.applicationservice.core.valueobject;

import org.springframework.data.domain.Page;

import java.util.List;

public class PageResult<T> {

   private final List<T> content;
   private final long totalElements;
   private final int offset;
   private final int limit;

   public PageResult(List<T> content, long totalElements, int offset, int limit) {
      this.content = content;
      this.totalElements = totalElements;
      this.offset = offset;
      this.limit = limit;
   }

   public List<T> getContent() {
      return content;
   }

   public long getTotalElements() {
      return totalElements;
   }

   public int getOffset() {
      return offset;
   }

   public int getLimit() {
      return limit;
   }

   public static <T> PageResult<T> fromPage(Page<T> page) {
      int offset = page.getNumber() * page.getSize();
      int limit = page.getSize();
      return new PageResult<>(page.getContent(), page.getTotalElements(), offset, limit);
   }
}
