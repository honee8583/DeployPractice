var main = {
    init : function () {
        var _this = this;

        // 글 등록 버튼 이벤트 추가.
        $('#btn-save').on('click', function () {
            _this.save();
        });
    },
    // 게시글 등록 메소드
    save : function () {
        var data = {
            title: $('#title').val(),
            author: $('#author').val(),
            content: $('#content').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/post',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('글이 등록되었습니다.');
            // 글 등록에 성공하면 메인페이지로 이동.
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();