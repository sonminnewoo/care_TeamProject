var slideIndex = 0;
var slideshowInterval;
showSlides();

function showSlides() {
	var i;
	var slides = document.getElementsByClassName("mySlides");
	var dots = document.getElementsByClassName("dot");
	for (i = 0; i < slides.length; i++) {
		slides[i].style.display = "none";
		slides[i].classList.remove("active"); // 기존 active 클래스 제거
	}
	for (i = 0; i < dots.length; i++) {
		dots[i].className = dots[i].className.replace(" active", "");
	}
	slideIndex++;
	if (slideIndex > slides.length) { slideIndex = 1 }
	slides[slideIndex - 1].style.display = "block";
	dots[slideIndex - 1].className += " active";
		clearTimeout(slideshowInterval); // 딜레이 초기화
	slideshowInterval = setTimeout(showSlides, 3000); // 3초마다 이미지 변경
}

// 불릿 클릭 시 해당 슬라이드로 이동
var dots = document.getElementsByClassName("dot");
for (var j = 0; j < dots.length; j++) {
	dots[j].addEventListener("click", function() {
		var dots = document.getElementsByClassName("dot");
		for (var k = 0; k < dots.length; k++) {
			if (dots[k] == this) {
				slideIndex = k;
				showSlides();
			}
		}
	});
}

