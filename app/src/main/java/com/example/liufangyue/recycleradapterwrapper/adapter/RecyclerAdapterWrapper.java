package com.example.liufangyue.recycleradapterwrapper.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * RecyclerView.Adapter的封装类,简化Adapter的创建
 * Created by yudongbo on 2015/10/20.
 */
public abstract class RecyclerAdapterWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static interface OnRecyclerViewClickListener {
        void onClick(int position);
    }

    public class ViewHolderWrapper extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View root;
        public int position;
        public SparseArray<View> viewSparseArray;

        public ViewHolderWrapper(View itemView) {
            super(itemView);
            this.root = itemView;
            viewSparseArray = new SparseArray<>();
            itemView.setOnClickListener(this);
        }

        public View findViewById(int id) {
            View v = viewSparseArray.get(id);
            if (v == null) {
                v = root.findViewById(id);
                viewSparseArray.put(id, v);
            }
            return v;
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onClick(position);
            }
        }
    }

    public class HeadViewHolder extends RecyclerView.ViewHolder {

        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

    public RecyclerAdapterWrapper(List<T> personList, int itemViewLayoutId) {
        this.itemList = personList;
        this.mItemViewLayoutId = itemViewLayoutId;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeadView == null && mFootView == null) {
            return ITEM_TYPE;
        } else if (mHeadView != null && mFootView != null) {
            if (position == 0) {
                return HEAD_TYPE;
            } else if (position == getItemCount() - 1) {
                return FOOT_TYPE;
            } else {
                return ITEM_TYPE;
            }
        } else if (mHeadView != null) {
            if (position == 0) {
                return HEAD_TYPE;
            } else {
                return ITEM_TYPE;
            }
        } else if (mFootView != null) {
            if (position == getItemCount() - 1) {
                return FOOT_TYPE;
            } else {
                return ITEM_TYPE;
            }
        }
        return ITEM_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = null;
        switch (viewType) {
            case HEAD_TYPE:
                v = mHeadView;
                return new HeadViewHolder(v);
            case ITEM_TYPE:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(mItemViewLayoutId, viewGroup, false);
                return new ViewHolderWrapper(v);
            case FOOT_TYPE:
                v = mFootView;
                return new FootViewHolder(v);
        }
        return new ViewHolderWrapper(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (getItemViewType(i) == ITEM_TYPE) {
            if (mHeadView != null) {
                i--;
            }
            ViewHolderWrapper vhw = (ViewHolderWrapper) viewHolder;
            T data = itemList.get(i);
            vhw.position = i;
            initData(vhw, data, i);
        }
    }

    public abstract void initData(ViewHolderWrapper holderWrapper, T item, int position);

    @Override
    public int getItemCount() {
        if (mHeadView == null && mFootView == null) {
            return itemList.size();
        } else if (mHeadView != null && mFootView != null) {
            return itemList.size() + 2;
        } else {
            return itemList.size() + 1;
        }
    }

    public void setOnRecyclerViewClickListener(OnRecyclerViewClickListener listener) {
        this.mListener = listener;
    }

    public void remove(int i) {
        if (mHeadView == null) {
            notifyItemRemoved(i);
            itemList.remove(i);
            notifyItemRangeChanged(i, getItemCount() - i - (mFootView == null ? 0 : 1));
        } else {
            notifyItemRemoved(i + 1);
            itemList.remove(i);
            notifyItemRangeChanged(i + 1, getItemCount() - i - (mFootView == null ? 0 : 2));
        }
    }

    public void replace(int i, T t) {
        remove(i);
        insert(i, t);
    }

    public void insert(int i, T p) {
        if (mHeadView == null) {
            notifyItemInserted(i);
            itemList.add(i, p);
            notifyItemRangeChanged(i, itemList.size() - i);
        } else {
            notifyItemInserted(i + 1);
            itemList.add(i, p);
            notifyItemRangeChanged(i + 1, itemList.size() - i);
        }
    }

    public void clear() {
        itemList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<T> data) {
        this.itemList.addAll(data);
        notifyDataSetChanged();
    }

    public void add(T data) {
        insert(getItemCount(), data);
    }

    public void setHeadView(View headView) {
        this.mHeadView = headView;
    }

    public void setFootView(View footView) {
        this.mFootView = footView;
    }

    private List<T> itemList;
    private OnRecyclerViewClickListener mListener;
    private int mItemViewLayoutId;
    private View mHeadView;
    private View mFootView;
    private final int HEAD_TYPE = 0;
    private final int ITEM_TYPE = 1;
    private final int FOOT_TYPE = 2;
}
