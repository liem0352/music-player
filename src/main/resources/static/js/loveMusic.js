let n;

let myFlag = false;
// 页面数据加载
function listLoad (u){
    jQuery.ajax({
        url : u,
        type : "get",
        success : function(obj) {

            n = obj.status;
            if(obj.status === 1){

                mData = [];
                artData = [];
                mUrl = [];
                pData = [];
                
                // 拿到查到的所有音乐数据的条数
                let number = obj.data.num;
                let totalPage = document.querySelector("#totalPage");
                // 每页最多放8条，结果向上取整
                totalPage.value = Math.ceil(number/8);

                // 获取每条数据的信息，如：musicName、musicSinger、musicUrl，这些播放器需要使用
                for(let i = 0 ; i < obj.data.musicList.length; i++){
                    let mus = obj.data.musicList[i];
                    mData[i] = mus.mname;
                    artData[i] = mus.msinger;
                    mUrl[i] = mus.url;

                    // 专门用来定位 当前应该操作哪个按钮，格式：#p+musicId.
                    pData[i] = "#p"+mus.mid;


                }
                // 将接收到的数据挂到 dom 树上
                dealWithData(obj.data.musicList);

                // 刷新页面之后走这里
                if(myFlag == false && u.substring(0,30) === "lovemusic/getlovemusic?pageNum"){
                    initPlayer(mData,artData,mUrl,pData);
                    myFlag = true;
                }else{
                    // 点击查询后的走这里
                    iniT(mData,artData,mUrl,pData);
                }

            }else{
                console.log(obj.msg);
            }
        },
        error:function(obj){
            console.log("请求失败"+obj);
        }
    });
}

// 将数据挂载到 dom 树上
function dealWithData(list){
    // 将来挂载的父节点
    let info = document.querySelector("#info");
    // 先将原先的数据清空一下
    info.innerHTML = "";
    let s = "";
    for(let i = 0; i < list.length; i++){
        let elem = list[i];
        s += '<tr>';
        s += '<td hidden="hidden">'+elem.mid+'</td>';
        s += '<td>'+elem.mname+'</td>';
        s += '<td>'+elem.msinger+'</td>';
        s += '<td>'+elem.userInfo.username+'</td>';
        s += '<td>'+elem.createtime+'</td>';
        s += '<td> <button class = "btn btn-primary" id = "p'+elem.mid+'" onclick="playerSong(\''+elem.url+'\','+elem.mid+')"> 播放 </button>' +'</td>';
        s += '<td> <button class = "btn btn-primary"  onclick="deleteInfo('+elem.mid+')"> 移除 </button></td>';
        s += "</tr>"

        
    }
    $("#info").html(s);
}


// 查询音乐
function queryMusic(){

    // 先关闭音乐播放器
    bang();
    
    let musicName = document.querySelector(".form-inline #exampleInputName2");
    let nameM = musicName.value;
    if(nameM.trim() === ""){
        // 将列表清空一下
        let infoDiv = document.querySelector("#info");
        infoDiv.innerHTML = "";
        //查询全部
        listLoad("lovemusic/getlovemusic?pageNum=1&pageSize=8");
    }else{
        // 将列表清空一下
        let infoDiv = document.querySelector("#info");
        infoDiv.innerHTML = "";
        //根据名字进行模糊查询
        listLoad("lovemusic/getlovemusic?musicname="+nameM+"&pageNum=1&pageSize=8");
    }
}

// 将音乐移出收藏夹
function deleteInfo(id){
    jQuery.ajax({
        url : "lovemusic/removelovemusic",
        type : "post",
        data : {
            "id":id
        },
        success : function(obj){
            if(obj.status === 1){
                bang();
                alert("id:"+id+"的音乐,移出收藏夹成功!");
                listLoad("lovemusic/getlovemusic?pageNum=1&pageSize=8");

                // 检查收藏夹里还有没有音乐
                if(obj.data.musicTotal === 0){
                    location.reload();
                }
            }else{
                console.log(obj.msg);
            }
        },
        error:function(obj){
            console.log("请求失败!"+obj);
        }
    });
}


//  ---------------------------- 以下为分页逻辑 ----------------------------

// // 跳转到第一页
function first_click(){
    bang();
    let currentPage = document.querySelector("#currentPage");

    currentPage.value = 1;
    // 直接调用初始化方法就行
    listLoad("lovemusic/getlovemusic?pageNum=1&pageSize=8");
}

//跳转到最后一页
function last_click(){
    bang();
    let totalPage = document.querySelector("#totalPage");
    let currentPage = document.querySelector("#currentPage");


    currentPage.value = totalPage.value;
    jQuery.ajax({
        url : "lovemusic/getlovemusic?pageNum="+totalPage.value+"&pageSize=8",
        type : "get",
        success : function(obj) {

            if(obj.status === 1){

                mData = [];
                artData = [];
                mUrl = [];
                pData = [];
                
                let number = obj.data.num;
                let totalPage = document.querySelector("#totalPage");
                totalPage.value = Math.ceil(number/8);


                for(let i = 0 ; i < obj.data.musicList.length; i++){
                    let mus = obj.data.musicList[i];
                    mData[i] = mus.mname;
                    artData[i] = mus.msinger;
                    mUrl[i] = mus.url;

                    pData[i] = "#p"+mus.mid;


                }

                // 将接收到的数据挂到 dom 树上
                dealWithData(obj.data.musicList);

                // 点击查询后的走这里
                iniT(mData,artData,mUrl,pData);

            }else{
                console.log(obj.msg);
            }
        },
        error:function(obj){
            console.log("请求失败"+obj);
        }
    });
}


// 前一页
function prev_click(){
    bang();
    let currentPage = document.querySelector("#currentPage");
    let xianzai  = currentPage.value;

    if(Number(xianzai) > 1){
        currentPage.value = Number(xianzai) - 1;
        xianzai = Number(xianzai) - 1;
    }else{
        return;
    }
    jQuery.ajax({
        url : "lovemusic/getlovemusic?pageNum="+xianzai+"&pageSize=8",
        type : "get",
        success : function(obj) {

            if(obj.status === 1){

                mData = [];
                artData = [];
                mUrl = [];
                pData = [];
                
                let number = obj.data.num;
                let totalPage = document.querySelector("#totalPage");
                totalPage.value = Math.ceil(number/8);


                for(let i = 0 ; i < obj.data.musicList.length; i++){
                    let mus = obj.data.musicList[i];
                    mData[i] = mus.mname;
                    artData[i] = mus.msinger;
                    mUrl[i] = mus.url;

                    pData[i] = "#p"+mus.mid;


                }

                // 将接收到的数据挂到 dom 树上
                dealWithData(obj.data.musicList);

                // 点击查询后的走这里
                iniT(mData,artData,mUrl,pData);

            }else{
                console.log(obj.msg);
            }
        },
        error:function(obj){
            console.log("请求失败"+obj);
        }
    });
}

// 后一页
function next_click(){
    bang();
    let totalPage = document.querySelector("#totalPage");
    let currentPage = document.querySelector("#currentPage");

    // 当前的页数
    let xianzai  = currentPage.value;
    // 总的页数
    let zongdu = totalPage.value;

    // 检测是否还有下一页
    if(Number(xianzai) < zongdu){
        currentPage.value = Number(xianzai) + 1;
        xianzai = Number(xianzai) + 1;
    }else{
        // 没有就直接结束
        return;
    }
    jQuery.ajax({
        url : "lovemusic/getlovemusic?pageNum="+xianzai+"&pageSize=8",
        type : "get",
        success : function(obj) {

            if(obj.status === 1){

                mData = [];
                artData = [];
                mUrl = [];
                pData = [];
                
                let number = obj.data.num;
                let totalPage = document.querySelector("#totalPage");
                totalPage.value = Math.ceil(number/8);


                for(let i = 0 ; i < obj.data.musicList.length; i++){
                    let mus = obj.data.musicList[i];
                    mData[i] = mus.mname;
                    artData[i] = mus.msinger;
                    mUrl[i] = mus.url;

                    pData[i] = "#p"+mus.mid;


                }

                // 将接收到的数据挂到 dom 树上
                dealWithData(obj.data.musicList);

                // 点击查询后的走这里
                iniT(mData,artData,mUrl,pData);

            }else{
                console.log(obj.msg);
            }
        },
        error:function(obj){
            console.log("请求失败"+obj);
        }
    });
}



// 主动选择页数
function choose_page(){
    // 如果播放器正在播放音乐，就将它关闭
    bang();
    let currentPage = document.querySelector("#currentPage");
    let totalPage = document.querySelector("#totalPage");

    
    let xianzai  = currentPage.value;
    let zongdu = totalPage.value;

    // 检查范围时候合适
    if(xianzai < 1 || xianzai > zongdu){
        alert("页数不合法!");
        // 转到第一页
        first_click();
        return;
    }

    if(xianzai === 1){
        first_click();

    }

    if(xianzai === zongdu){
        last_click();

    }else{
        jQuery.ajax({
            url : "lovemusic/getlovemusic?pageNum="+xianzai+"&pageSize=8",
            type : "get",
            success : function(obj) {
    
                if(obj.status === 1){
    
                    mData = [];
                    artData = [];
                    mUrl = [];
                    pData = [];
                    
                    let number = obj.data.num;
                    let totalPage = document.querySelector("#totalPage");
                    totalPage.value = Math.ceil(number/8);
    
    
                    for(let i = 0 ; i < obj.data.musicList.length; i++){
                        let mus = obj.data.musicList[i];
                        mData[i] = mus.mname;
                        artData[i] = mus.msinger;
                        mUrl[i] = mus.url;
    
                        pData[i] = "#p"+mus.mid;
    
    
                    }
    
                    // 将接收到的数据挂到 dom 树上
                    dealWithData(obj.data.musicList);

                    // 点击查询后的走这里
                    iniT(mData,artData,mUrl,pData);
    
                }else{
                    console.log(obj.msg);
                }
            },
            error:function(obj){
                console.log("请求失败"+obj);
            }
        });
    }
}