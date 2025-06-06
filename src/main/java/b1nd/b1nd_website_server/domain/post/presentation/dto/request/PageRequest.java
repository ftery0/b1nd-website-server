package b1nd.b1nd_website_server.domain.post.presentation.dto.request;

public record PageRequest(
        Long page,
        Long size) {
    public static PageRequest of(final long page, final long size) {
        return new PageRequest(page, size);
    }

    public void validate() {
        if(page<1) {
            throw new IllegalArgumentException("Page must over than 0");
        }
        if(size<1) {
            throw new IllegalArgumentException("Size must over than 0");
        }
    }
}
