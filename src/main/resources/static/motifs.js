window.addEventListener('DOMContentLoaded', function () {
    var motifs = document.querySelectorAll('.motif');

    function positionnerMotifs() {
        motifs.forEach(function (motif) {
            var motifOffsetTop = motif.offsetTop;
            var motifHeight = motif.offsetHeight;
            var windowHeight = window.innerHeight;

            var depassement = Math.max(0, motifHeight - windowHeight);
            var translateY = -Math.min(depassement, window.pageYOffset - motifOffsetTop);

            motif.style.transform = 'translateY(' + translateY + 'px)';
        });
    }

    positionnerMotifs();

    window.addEventListener('scroll', positionnerMotifs);
    window.addEventListener('resize', positionnerMotifs);
});