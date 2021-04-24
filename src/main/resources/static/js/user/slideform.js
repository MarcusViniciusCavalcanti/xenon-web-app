const sliderItems = $('.items');

const Slider = {
    currentItem: 0,
    init: function () {
        Slider.in(Slider.currentItem)
    },
    in: function (index) {
        const sliderItem = sliderItems[index];
        TweenMax.set(sliderItem, {scale: .9})

        const timeline = new TimelineMax();

        timeline
        .to(sliderItem, .5, {left: 0, delay: .2})
        .to(sliderItem, .5, {scale: 1})
    },
    out: function (index, nextIndex) {
        const sliderItem = sliderItems[index];
        const timeline = new TimelineMax();

        timeline
        .to(sliderItem, .5, {scale: .9})
        .to(sliderItem, .5, {left: '100vw'})
        .call(Slider.in, [nextIndex], this)
    },

    next: function () {
        const next = Slider.currentItem + 1;
        Slider.out(Slider.currentItem, next)
        Slider.currentItem = next;
    },
}

Slider.init()
