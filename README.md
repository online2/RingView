1.自定义进度圆环，最多支持嵌套四个圆环<br>
        按照惯例先扔上效果图<br>
        (1)具体效果图<br>
             ![image](https://github.com/online2/RingView/tree/master/app/src/main/res/drawable-xxhdpi/ringview.gif) <br>
             
        方法属性<br>
        (2) 提供如下自定义属性，设置不同样式，颜色<br>
          <!--内层圆环宽度-->
                <attr name="inner_stroke_width" format="dimension"/><br>
                <!--内层圆环底层宽度--><br>
                <attr name="inner_stroke_width_unfinished" format="dimension"/><br>
                <!--最外层圆环宽度--><br>
                <attr name="outer_stroke_width" format="dimension"/><br>
                <!--最外层圆环底层宽度--><br>
                <attr name="outer_stroke_width_unfinished" format="dimension"/><br>
                <!--圆环填充色底色--><br>
                <attr name="unfinished_color" format="color"/><br>
                <!--默认点击时其他圆环进度填充色--><br>
                <attr name="default_filled_color" format="color"/><br>
                <!--第四个圆环颜色--><br>
                <attr name="inner_first_color" format="color"/><br>
                <!--第三个圆环颜色--><br>
                <attr name="inner_second_color" format="color"/><br>
                <!--第二个圆环颜色--><br>
                <attr name="inner_third_color" format="color"/><br>
                <!--第一个圆环颜色--><br>
                <attr name="overall_color" format="color"/><br>
                <!--对应圆环进度--><br>
                <attr name="inner_first_progress" format="float"/><br>
                <attr name="inner_second_progress" format="float"/><br>
                <attr name="inner_third_progress" format="float"/><br>
                <attr name="overall_progress" format="float"/><br>
                <!--是否显示对应圆环--><br>
                <attr name="inner_first_show" format="boolean"/><br>
                <attr name="inner_second_show" format="boolean"/><br>
                <attr name="inner_third_show" format="boolean"/><br>
                <attr name="filled_show" format="boolean"/><br>
                <!--圆环是否可以点击--><br>
                <attr name="ring_is_clickable" format="boolean"/><br>
                
        (3) 提供一系列设置方法<br> 
            如：设置进度setRingOverallProgress<br> 
            highlight()设置高亮，取消高亮unhighlight();<br>
            等等具体可以看详细代码
            
            
       
            