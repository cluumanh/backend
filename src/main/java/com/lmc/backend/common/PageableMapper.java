package com.lmc.backend.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Set;

public final class PageableMapper {

    private PageableMapper() {
    }

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final String DEFAULT_DIRECTION = "asc";
    private static final String TIE_BREAK_FIELD = "id";

    public static Pageable toPageable(
            Integer page,
            Integer size,
            String sortStr,
            Set<String> allowedFields,
            Sort defaultSort,
            int maxSize
    ) {
        int normalizedSize = normalizeSize(size, maxSize);
        int normalizedPage = normalizePage(page);

        Sort sort = parseSort(sortStr, allowedFields, defaultSort);
        return PageRequest.of(normalizedPage, normalizedSize, sort);
    }

    private static int normalizeSize(Integer size, int maxSize) {
        int result = (size == null ? DEFAULT_SIZE : size);
        return Math.min(Math.max(result, 1), maxSize);
    }

    private static int normalizePage(Integer page) {
        int result = (page == null ? DEFAULT_PAGE : page);
        return Math.max(result - 1, 0);
    }

    private static Sort parseSort(
            String sortStr,
            Set<String> allowedFields,
            Sort fallback
    ) {
        if (sortStr == null || sortStr.isBlank()) {
            return ensureTieBreak(fallback, allowedFields);
        }

        String[] clauses = sortStr.split(";");
        Sort result = Sort.unsorted();

        for (String clause : clauses) {
            String normalizedClause = clause.trim();
            if (normalizedClause.isEmpty()) {
                continue;
            }

            String[] parts = normalizedClause.split(",");
            String field = parts[0].trim();
            String direction = parts.length > 1 ? parts[1].trim() : DEFAULT_DIRECTION;

            if (!allowedFields.contains(field)) {
                return ensureTieBreak(fallback, allowedFields);
            }

            Sort.Direction sortDirection =
                    "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

            result = result.and(Sort.by(sortDirection, field));
        }

        if (result.isUnsorted()) {
            return ensureTieBreak(fallback, allowedFields);
        }

        return ensureTieBreak(result, allowedFields);
    }

    private static Sort ensureTieBreak(Sort sort, Set<String> allowedFields) {
        if (!allowedFields.contains(TIE_BREAK_FIELD)) {
            return sort;
        }

        if (!containsField(sort, TIE_BREAK_FIELD)) {
            return sort.and(Sort.by(Sort.Direction.DESC, TIE_BREAK_FIELD));
        }

        return sort;
    }

    private static boolean containsField(Sort sort, String field) {
        for (Sort.Order order : sort) {
            if (order.getProperty().equals(field)) {
                return true;
            }
        }
        return false;
    }
}
