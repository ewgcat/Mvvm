package com.lishuaihua.imageloader.svg

import com.lishuaihua.imageloader.svg.utils.RenderOptionsBase

/**
 * A fluent builder class that creates a render configuration object for the
 * [SVG.renderToCanvas] and [SVG.renderToPicture] methods.
 *
 * <h3>Example usage</h3>
 *
 * <pre class="code-block">
 * `RenderOption renderOptions = RenderOptions.create();
 * renderOptions.viewPort(100f, 100f, 400f, 300f)   // Set the area of the Canvas to render the SVG into
 * .css("rect { fill: red; }")         // Add some CSS that makes all rectangles red
 * svg.renderToCanvas(canvas, renderOptions);
` *
</pre> *
 */
class RenderOptions : RenderOptionsBase {
    /**
     * Create a new `RenderOptions` instance.  You can choose to use either this constructor,
     * or `new RenderOptions.create()`.  Both are equivalent.
     */
    constructor() : super() {}

    /**
     * Creates a copy of the given `RenderOptions` object.
     * @param other the object to copy
     */
    constructor(other: RenderOptions?) : super(other) {}

    /**
     * Specifies some additional CSS rules that will be applied during render in addition to
     * any specified in the file itself.
     * @param css CSS rules to apply
     * @return this same `RenderOptions` instance
     */
    override fun css(css: String): RenderOptions {
        return super.css(css) as RenderOptions
    }

    /**
     * Returns true if this RenderOptions instance has had CSS set with `css()`.
     * @return true if this RenderOptions instance has had CSS set
     */
    override fun hasCss(): Boolean {
        return super.hasCss()
    }

    /**
     * Specifies how the renderer should handle aspect ratio when rendering the SVG.
     * If not specified, the default will be `PreserveAspectRatio.LETTERBOX`. This is
     * equivalent to the SVG default of `xMidYMid meet`.
     * @param preserveAspectRatio the new aspect ration value
     * @return this same `RenderOptions` instance
     */
    override fun preserveAspectRatio(preserveAspectRatio: PreserveAspectRatio): RenderOptions {
        return super.preserveAspectRatio(preserveAspectRatio) as RenderOptions
    }

    /**
     * Returns true if this RenderOptions instance has had a preserveAspectRatio value set with `preserveAspectRatio()`.
     * @return true if this RenderOptions instance has had a preserveAspectRatio value set
     */
    override fun hasPreserveAspectRatio(): Boolean {
        return super.hasPreserveAspectRatio()
    }

    /**
     * Specifies the `id` of a `<view>` element in the SVG.  A `<view>`
     * element is a way to specify a predetermined view of the document, that differs from the default view.
     * For example it can allow you to focus in on a small detail of the document.
     *
     * Note: setting this option will override any [.viewBox] or [.preserveAspectRatio] settings.
     *
     * @param viewId the id attribute of the view that should be used for rendering
     * @return this same `RenderOptions` instance
     */
    override fun view(viewId: String): RenderOptions {
        return super.view(viewId) as RenderOptions
    }

    /**
     * Returns true if this RenderOptions instance has had a view set with `view()`.
     * @return true if this RenderOptions instance has had a view set
     */
    override fun hasView(): Boolean {
        return super.hasView()
    }

    /**
     * Specifies alternative values to use for the root element `viewBox`. Any existing `viewBox`
     * attribute value will be ignored.
     *
     * Note: will be overridden if a [.view] is set.
     *
     * @param minX The left X coordinate of the viewBox
     * @param minY The top Y coordinate of the viewBox
     * @param width The width of the viewBox
     * @param height The height of the viewBox
     * @return this same `RenderOptions` instance
     */
    override fun viewBox(minX: Float, minY: Float, width: Float, height: Float): RenderOptions {
        return super.viewBox(minX, minY, width, height) as RenderOptions
    }

    /**
     * Returns true if this RenderOptions instance has had a viewBox set with `viewBox()`.
     * @return true if this RenderOptions instance has had a viewBox set
     */
    override fun hasViewBox(): Boolean {
        return super.hasViewBox()
    }

    /**
     * Describes the viewport into which the SVG should be rendered.  If this is not specified,
     * then the whole of the canvas will be used as the viewport.  If rendering to a `Picture`
     * then a default viewport width and height will be used.
     *
     * @param minX The left X coordinate of the viewport
     * @param minY The top Y coordinate of the viewport
     * @param width The width of the viewport
     * @param height The height of the viewport
     * @return this same `RenderOptions` instance
     */
    override fun viewPort(minX: Float, minY: Float, width: Float, height: Float): RenderOptions {
        return super.viewPort(minX, minY, width, height) as RenderOptions
    }

    /**
     * Returns true if this RenderOptions instance has had a viewPort set with `viewPort()`.
     * @return true if this RenderOptions instance has had a viewPort set
     */
    override fun hasViewPort(): Boolean {
        return super.hasViewPort()
    }

    /**
     * Specifies the `id` of an element, in the SVG, to treat as the target element when
     * using the `:target` CSS pseudo class.
     *
     * @param targetId the id attribute of an element
     * @return this same `RenderOptions` instance
     */
    override fun target(targetId: String): RenderOptions {
        return super.target(targetId) as RenderOptions
    }

    /**
     * Returns true if this RenderOptions instance has had a target set with `target()`.
     * @return true if this RenderOptions instance has had a target set
     */
    override fun hasTarget(): Boolean {
        return super.hasTarget()
    }

    companion object {
        /**
         * Create a new `RenderOptions` instance.  This is just an alternative to `new RenderOptions()`.
         * @return new instance of this class.
         */
        @JvmStatic
        fun create(): RenderOptions {
            return RenderOptions()
        }
    }
}