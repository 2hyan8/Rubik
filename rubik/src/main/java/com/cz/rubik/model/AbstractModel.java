package com.cz.rubik.model;

import androidx.core.util.Consumer;

import com.cz.rubik.function.BiConsumer;
import com.cz.rubik.function.FouConsumer;
import com.cz.rubik.function.Function;
import com.cz.rubik.matrix.Transform;
import com.cz.rubik.utils.tools.ArraysUtils;

/*
 * 抽象模型类
 * 定义了模型可能会使用的接口
 */
public abstract class AbstractModel implements IModel {
    protected int mId = 0; // 模型id
    protected int[] mLocation = ArraysUtils.getArr(0, 0, 0); // 当前模型在父模型中的位置
    protected AbstractModel mParent = null; // 父模型
    protected Transform mTransform; // 坐标变换

    public AbstractModel() {}

    public AbstractModel(int id, int[] location, AbstractModel parent) {
        mId = id;
        mLocation = location;
        mParent = parent;
        mTransform = getTransform();
    }

    // 获取模型变换
    public Transform getTransform() {
        if (mTransform == null) {
            return mParent.getTransform();
        }
        return mTransform;
    }

    // 获取模型中心坐标
    public float[] getCenter() {return ArraysUtils.getArr(0f, 0f, 0f);};

    // 设置模型位置
    public void setLocation(int[] location) {
        mLocation = location;
    }

    // 获取模型位置
    public int[] getLocation() {
        return mLocation;
    }

    // 获取父模型位置
    public int[] getParentLocation() {
        return mParent.getLocation();
    }

    public void forStubModels(IModel[] stubs, Consumer<IModel> consumer) {
        for (int i = 0; i < stubs.length; i++) {
            consumer.accept(stubs[i]);
        }
    }

    public void forStubModels(IModel[][] stubs, Consumer<IModel> consumer) {
        for (int i = 0; i < stubs.length; i++) {
            for (int j = 0; j < stubs[i].length; j++) {
                consumer.accept(stubs[i][j]);
            }
        }
    }

    public void forStubModels(IModel[][][] stubs, Consumer<IModel> consumer) {
        for (int i = 0; i < stubs.length; i++) {
            for (int j = 0; j < stubs[i].length; j++) {
                for (int k = 0; k < stubs[i][j].length; k++) {
                    consumer.accept(stubs[i][j][k]);
                }
            }
        }
    }

    public void setToStubModels(AbstractModel[] stubs, BiConsumer<AbstractModel, Integer> consumer) {
        for (int i = 0; i < stubs.length; i++) {
            consumer.accept(stubs[i], i);
        }
    }

    public void setToStubModels(AbstractModel[][][] stubs, FouConsumer<AbstractModel, Integer> consumer) {
        for (int i = 0; i < stubs.length; i++) {
            for (int j = 0; j < stubs[i].length; j++) {
                for (int k = 0; k < stubs[i][j].length; k++) {
                    consumer.accept(stubs[i][j][k], i, j, k);
                }
            }
        }
    }

    public <T> void setToSquares(Square[] squares, T[] features, BiConsumer<Square, T> consumer) {
        for (int i = 0; i < squares.length; i++) {
            consumer.accept(squares[i], features[i]);
        }
    }

    public <T> void setToSquares(Square[][] squares, T[][] features, BiConsumer<Square, T> consumer) {
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                consumer.accept(squares[i][j], features[i][j]);
            }
        }
    }

    public <R> R[] getFromSquares(Square[] squares, Class clazz, Function<Square, R> function) {
        R[] features = ArraysUtils.newArr(clazz, squares.length);
        for (int i = 0; i < squares.length; i++) {
            features[i] = function.apply(squares[i]);
        }
        return features;
    }

    public <R> R[][] getFromSquares(Square[][] squares, Class clazz, Function<Square, R> function) {
        R[][] features = ArraysUtils.newArr(clazz, squares.length, squares.length);
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                features[i][j] = function.apply(squares[i][j]);
            }
        }
        return features;
    }
}
