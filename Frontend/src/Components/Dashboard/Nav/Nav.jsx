import React from "react";
import {
  Navbar,
  NavbarBrand,
  NavbarContent,
  NavbarItem,
  Link,
  DropdownItem,
  DropdownTrigger,
  Dropdown,
  DropdownMenu,
  Avatar,
  NavbarMenuItem,
  NavbarMenuToggle,
  Button,
  NavbarMenu,
} from "@nextui-org/react";
import HomeLogo from "./HomeLogo";
import { useAxios } from "../../../Security/axios/AxiosProvider";
import Cookies from "js-cookie";
import useUser from "../../../Security/hooks/useUser";

const Nav = ({ onLoginOpen, onRegisterOpen, userData, setUserData }) => {
  const [isMenuOpen, setIsMenuOpen] = React.useState(false);
  const apiClient = useAxios();

  const menuItems = [
    "Profile",
    "Dashboard",
    "Activity",
    "Analytics",
    "System",
    "Deployments",
    "My Settings",
    "Team Settings",
    "Help & Feedback",
    "Log Out",
  ];

  const logout = async () => {
    const response = await apiClient.post("/auth/logout");

    if (response.status === 200) {
      Cookies.remove("refresh_token");
      localStorage.removeItem("accessToken");
      setUserData({});
      console.log("Logged out");
    }
  };

  return (
    <Navbar maxWidth="xl" onMenuOpenChange={setIsMenuOpen}>
      <NavbarContent>
        <NavbarMenuToggle
          aria-label={isMenuOpen ? "Close menu" : "Open menu"}
          className="sm:hidden"
        />
        <NavbarBrand>
          <HomeLogo />
        </NavbarBrand>
      </NavbarContent>

      <NavbarContent className="hidden sm:flex gap-4" justify="center">
        <NavbarItem>
          <Link color="foreground" href="#">
            Find Roommates
          </Link>
        </NavbarItem>
        <NavbarItem isActive>
          <Link href="#" aria-current="page">
            Agreements
          </Link>
        </NavbarItem>
        <NavbarItem>
          <Link color="foreground" href="#">
            Past Matches
          </Link>
        </NavbarItem>
      </NavbarContent>
      <NavbarContent justify="end">
        <NavbarItem className="hidden lg:flex">
          <Button color="primary" onPress={onLoginOpen}>
            Login
          </Button>
        </NavbarItem>
        <NavbarItem>
          <Button
            onPress={onRegisterOpen}
            color="primary"
            href="#"
            variant="flat"
          >
            Sign Up
          </Button>
        </NavbarItem>

        {userData.username && (
          <Dropdown placement="bottom-end">
            <DropdownTrigger>
              <Avatar
                isBordered
                as="button"
                className="transition-transform"
                color="secondary"
                name="Jason Hughes"
                size="sm"
                src={userData.pfp}
              />
            </DropdownTrigger>
            <DropdownMenu aria-label="Profile Actions" variant="flat">
              <DropdownItem key="profile" className="h-14 gap-2">
                <p className="font-semibold">Signed in as</p>
                <p className="font-semibold">{userData.username}</p>
              </DropdownItem>
              <DropdownItem key="team_settings">Manage profile</DropdownItem>
              <DropdownItem
                onPress={() => logout()}
                key="logout"
                color="danger"
              >
                Log Out
              </DropdownItem>
            </DropdownMenu>
          </Dropdown>
        )}
      </NavbarContent>
    </Navbar>
  );
};

export default Nav;
