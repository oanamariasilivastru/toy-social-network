package com.example.demo11.repository.paging;

import com.example.demo11.domain.Entity;
import com.example.demo11.repository.Repository;

public interface PagingRepository<ID, E extends Entity<ID>>
{
    Page<E> findAllPaging(Pageable pageable);
}
