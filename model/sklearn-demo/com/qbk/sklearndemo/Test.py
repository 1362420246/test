
#!/usr/bin/python
# -*- coding:utf-8 -*-


from sklearn import tree
from sklearn2pmml.pipeline import PMMLPipeline
from sklearn2pmml import sklearn2pmml
from sklearn2pmml import make_pmml_pipeline    # 转换pkl文件为pmml_pipeline格式

import os
os.environ["PATH"] += os.pathsep + 'C:/Program Files/Java/jdk1.8.0_171/bin'

X=[[1,2,3,1],[2,4,1,5],[7,8,3,6],[4,8,4,7],[2,5,6,9]]
y=[0,1,0,2,1]
# PMMLPipeline只是处理estimator不能处理transformer
# 注意：有时候需要自定义函数加入到PMMLPipeline中，可以参考博客https://blog.csdn.net/weixin_38569817/article/details/87810658
pipeline = PMMLPipeline([("classifier", tree.DecisionTreeClassifier(random_state=9))])
pipeline.fit(X,y)

sklearn2pmml(pipeline, ".\demo.pmml", with_repr = True)
