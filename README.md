# RecyclerAdapterWrapper
封装RecyclerView的Adapter,简化创建过程，提供增，删，替换等基本操作
        adapter = new RecyclerAdapterWrapper<Person>(new ArrayList<Person>(), R.layout.recycler_item) {

            @Override
            public void initData(RecyclerAdapterWrapper.ViewHolderWrapper holderWrapper, Person person, int position) {
                TextView leftView = (TextView)holderWrapper.findViewById(R.id.left);
                TextView rightView = (TextView)holderWrapper.findViewById(R.id.right);
                leftView.setText(person.name);
                rightView.setText(person.phone);
            }
        };
        recyclerView.setAdapter(adapter);
继承RecyclerAdapterWrapper类,实现initData方法，即可创建RecyclerView的Adapter.
