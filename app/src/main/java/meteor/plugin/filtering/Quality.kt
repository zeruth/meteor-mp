package meteor.plugin.filtering

enum class Quality(value: Int) {
    NEAREST_NEIGHBOR(0),
    BILINEAR(1),
    BILINEAR_MIPMAP(2),
    BICUBIC(3)
}