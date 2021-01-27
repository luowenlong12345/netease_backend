## user
### /api/user/register  post
* required: username, password, roleId
### /api/user/login     post
* required: username, password
### /api/user/logout    post
* required: 
### /api/user/addRole   post
* required: roleId
### /api/user/recharge  post
* required: money

## role
### /api/role/all       get
* required: 

## goods
### /api/goods/add/     post
* required: image(图片), price, title
* optional: remark, content
### /api/goods/delete/  post
* required: goodsId

### /api/goods/get/      get
* required: goodsId

### /api/goods/list/      get
* required: page, size
* optional: order(desc, asc), orderBy(createTime, price, sellNumbers, title), sellerId

### /api/goods/update/   post
* required: id, price, title
* optional: remark, content

### /api/goods/updateImage/   post
* required: goodsId, image



## transaction
### /api/transaction/add/   post
* required: goodsId, number


### /api/transaction/list/      get
* required: page, size
* optional: order(desc, asc), orderBy(createTime, unitPrice), sellerId, buyerId


## shopping cart
### /api/cart/add    post
* required: goodsId, number
### /api/cart/delete    post 
* required ids
### /api/cart/buy    post 
* required ids