import React,{PureComponent,Component,Fragment} from 'react';
import PropTypes from 'prop-types';
import api from '../API';
import Main from '../Main'
import Modal from 'react-modal'
//modify setstate 제대로 들어가는지 확인

class ModifyStyle extends PureComponent{
    state={
        name:null,
        price:null,
        content:null,
        data:null,
        styleList:null,
        selectedStyle: null,
        selectedType: null,
    }
    open_modal_handler= async(i) => {
        console.log(i)
        if(i===undefined){ //create
            await this.setState({selectedType:0})
            this.open_modify_modal()

        }
        else{ //modify
            this.setState({selectedType:1, selectedStyle:i.id, name:i.name, price:i.price, content:i.content})
            await this.open_modify_modal()
            this.set_value()
        }
    }

    input_handler=(e)=>{
        const{name,value}=e.target
        this.setState({[name]:value})
    }
    set_value=()=>{
        console.log(document.getElementsByName('name'))
        document.getElementsByName('name')[0].value=this.state.name
        document.getElementsByName('price')[0].value=this.state.price
        document.getElementsByName('content')[0].value=this.state.content
    }
    getStyleListdata=()=>{
        api.get('/style').then(response =>         
            {
                //console.log(this.state.data)
                this.setState({ data:response.data.data })
            }
        )
    }
    requestCreateStyle=()=>{
        api.post('/style', {          
                "transaction_time": "2020-12-02T08:25:41.549",
                "result_code": "200",
                "description": "OK",
                "data": {
                    "name": this.state.name,
                    "status": "REGISTERED",
                    "price": this.state.price,
                    "content": this.state.content
                }           
        })
        .then(res => {
            console.log(res)
            if(res.data.description=='OK'){
                this.getStyleListdata()
            }
        })
    }
    requestModifyStyle=()=>{
        api.put('/style', {
            
            "transaction_time": "2020-12-02T08:25:41.549",
            "result_code": "200",
            "description": "OK",
            "data": {
                "id": 1,
                "name": this.state.name,
                "status": "REGISTERED",
                "price": this.state.price,
                "content": this.state.content
            }           
        })
        .then(res => {
            console.log(res)
            this.getStyleListdata()
        })
    }
    requestDeleteStyle=(id)=>{
        api.delete(`/style/${id}`)
        .then(res =>{
            console.log(res)
            alert('삭제완료')
            this.getStyleListdata()
        })
        
    }
    open_modify_modal=()=>{
        this.setState({modalOpen:true})
    }    
    close_modify_modal=()=>{
        this.setState({modalOpen:false})
    }
    button_handler=async()=>{
        const type=this.state.selectedType
        const name = document.getElementsByName("name")[0].value
        const price = document.getElementsByName("price")[0].value
        const content = document.getElementsByName("content")[0].value

        await this.setState({name:name, price:price, content:content})
        this.close_modify_modal()

        if(type===0){
            this.requestCreateStyle()
        }
        else if(type===1){
            this.requestModifyStyle()
        }
        else console.log('invalid select type')
    }
    render(){
        let styleList=[]
        if(this.state.data===null) {       
            this.getStyleListdata()       
        } 
        else{
            styleList= this.state.data.map((i) => 
                (   <div className="row">
                    <div className="col-sm-10">
                        <li className="component--item_card" onClick={()=>this.open_modal_handler(i)} >
                            <img src={i.img_url} className="image--itemcard col-sm-3" alt="" />
                            <div className="component--item_text">
                                <h3 >
                                    <span>{i.name}</span>
                                </h3>
                                <p > {i.price}</p>
                                <p >{i.content}</p>
                            </div>
                        </li></div>
                        <div className="col-sm-2" onClick={()=>this.requestDeleteStyle(i.id)}>&times;</div>
                    </div>
                )
                
            )
            this.state.styleList=styleList;
        }    

        return(      
            <div>
                <ul className="wrap_menu_list">                    
                        {this.state.styleList} 
                        <li className="component--item_card" onClick={()=>this.open_modal_handler()}>
                        <div className="col-sm-4"/>

                        <div className="col-sm-4">
                            <h1 >
                                +
                            </h1>
                        </div>
                        </li>   
                </ul> 
                <Modal className="bg-light modal-dialog modal-m" ariaHideApp={false} isOpen={this.state.modalOpen} onRequestClose={()=>this.close_modify_modal()}>
                    <div>
                    <div className="modal-content bg-light signupModal">
                                <div className="modal-header">                            <span className="close" onClick={()=>this.close_modify_modal()}>
                            &times;
                            </span>
                            <h4 className="modal-title">메뉴 설정</h4>
                                </div>
                                <div className="modal-body" onClick={()=>this.state.modalOpen}>
                                <div className="row">                                    
                                    <label className="col-sm-5">스타일 이름</label>                                    
                                    
                                    <input
                                        name="name"
                                        className="mod_input input_name"
                                        type="text"
                                        onChange={this.input_handler}
                                    
                                    /> 
                                </div>                                
                                <div className="row">                                    
                                    <label className="col-sm-5">가격</label>                                    
                                    
                                    <input
                                        name="price"
                                        className="mod_input input_price"
                                        type="number"
                                        onChange={this.input_handler}

                                    /> 
                                </div>
                                <div className="row">                                    
                                    <label className="col-sm-5">내용</label>
                                    
                                    <input
                                        name="content"
                                        className="mod_input input_content"
                                        type="text"
                                        onChange={this.input_handler}

                                    />  
                                    
                                </div>

                            </div>
                            <div className="modal-footer">
                            <button onClick={()=>this.button_handler()}>완료</button>
                            </div>
                            </div>

                        </div>
                </Modal>    
            </div>
        )
    }
}


export default ModifyStyle