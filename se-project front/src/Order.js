import React, { Component } from 'react';
import api from './API';

class Order extends Component{
    state={
        dishList:null,
        dish2amount:null,
    }
    
    getDishList(){
        api.get('/dish/1')//Q:id 왜? TODO: page, size 추가
        .then(response => {
            this.setState({dishList:response.data })
            console.log(this.state.dishList)
        })
    }
    calc_price(){
        //Q:price 측정해야하나?
    }
    setDishAmount(dish,amount){
        this.setState()
    }
//TODO: 이거 안됨 히히
    render(){
        let dish2amount=[]
        if(this.state.data===null){
            this.getDishList()
        }
        else{
            dish2amount=this.state.dishList.map((dish)=>
            (<li>
                <div>{dish.name}</div>
                <input onChange={this.setDishAmount()}>0</input>
            </li>))

            this.state.dish2amount=dish2amount
        }
        return(
            <div>
                <h1>수량 변경</h1>
                <div>
                    {this.state.dish2amount}
                </div>

            </div>
        )
    }
}


export default Order