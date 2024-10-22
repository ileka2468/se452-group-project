import React from "react";
import {
  User,
  Input,
  Divider,
  Listbox,
  ListboxItem,
  Card,
  CardBody,
  Select,
  SelectItem,
  ScrollShadow,
  Button
} from "@nextui-org/react";
import { users } from "./data";

const AgreementBox = () => {
    return(
        <div>
            <Card>
                <CardBody className="items-center">
                    Agreement Center
                </CardBody>
                <CardBody className="items-center">
                    <Select className="w-[500px] pb-5" items={users} placeholder="Select a User" renderValue={(items) => { return items.map((item) => (
                        <div key={item.key} className="flex items-center gap-2">
                            <User name={item.name} avatarProps={item.avatarProps}/>
                            <div className="flex flex-col">
                                <span>{item.data.name}</span>
                            </div>
                        </div>
                        
                    ));
                    }}
                    >   
                        {(user) => (
                            <SelectItem key={user.id}>
                                <User name={user.name} avatarProps={user.avatarProps}/>
                            </SelectItem>
                        )}
                    </Select>
                </CardBody>
            </Card>

            <Card>
                <CardBody className="items-center">
                    <ScrollShadow>
                        <Listbox className="w-[500px] h-[400px] size-fit py-8">
                    
                            <ListboxItem>This is an agreement.</ListboxItem>
                        
                        
                        </Listbox>
                    </ScrollShadow>
                </CardBody>
                <Divider />
                <CardBody className="items-center">
                    <Button>Accept</Button>
                    <Button>Decline</Button>
                </CardBody>
            </Card>

        </div>
    )
}

export default AgreementBox;