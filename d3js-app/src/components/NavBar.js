import { useState } from 'react';
import { Link } from "react-router-dom";

import {
	Collapse,
	Navbar,
	NavbarToggler,
	NavbarBrand,
	Nav,
	NavItem,
} from 'reactstrap';

export const NavBar = () => {
	const [isOpen, setIsOpen] = useState(false);
	const toggle = () => {
		setIsOpen(!isOpen);
	}
	return (
		<div>
			<Navbar color="light" light expand="md">
				<NavbarBrand href="/">TPA-GROUPE-2</NavbarBrand>
				<NavbarToggler onClick={toggle} />
				<Collapse isOpen={isOpen} navbar>
					<Nav className="ml-auto" navbar>
						<NavItem>
							<Link to="/" replace >Home</Link> |{" "}
						</NavItem>
						<NavItem>
							<Link to="/dataviz" replace >Data visualization</Link> |{" "}
						</NavItem>
						<NavItem>
							<Link to="/prediction" replace >Prediction</Link> |{" "}
						</NavItem>
						<NavItem>
							<Link to="/about" replace >About</Link>
						</NavItem>
					</Nav>
				</Collapse>
			</Navbar>
		</div>
	);
}