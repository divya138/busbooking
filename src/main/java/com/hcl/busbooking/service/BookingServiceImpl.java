package com.hcl.busbooking.service;


import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.BeanDefinitionDsl.BeanSupplierContext;
import org.springframework.stereotype.Service;

import com.hcl.busbooking.dao.BookingRepository;
import com.hcl.busbooking.dao.BusRouteRepository;
import com.hcl.busbooking.dao.UserRepository;
import com.hcl.busbooking.dto.BookingDto;
import com.hcl.busbooking.dto.BookingsDto;
import com.hcl.busbooking.model.Booking;
import com.hcl.busbooking.model.BusRoute;
import com.hcl.busbooking.model.User;
@Service
public class BookingServiceImpl implements BookingService{
	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	BusRouteRepository busRouteRepository;
	
	 BookingDto dto=new BookingDto();

	@Override
	public BookingDto bookTheSeat(BookingsDto bookingDto) {
		
		Booking booking =new Booking();
		BeanUtils.copyProperties(bookingDto, booking);
	
	Optional<User> optional	=userRepository.findById(bookingDto.getUserId());
	User user=null;
	if(optional.isPresent()) {
		user=optional.get();
	}
	if(user!=null) {
		
		Optional<BusRoute> option =busRouteRepository.findById(bookingDto.getBusServiceNumber());
		BusRoute bus=null;
		if(option.isPresent()) {
			bus=option.get();
		}
		
		bus.setAvailableSeats(bus.getAvailableSeats()-bookingDto.getNumberOfSeats());
		bus.setBusName(bus.getBusName());
		busRouteRepository.save(bus);
		booking.setUser(user);
		booking.setBusroute(bus);
		booking.setNoOfSeats(bookingDto.getNumberOfSeats());
		
		Booking booking1= bookingRepository.save(booking);
		dto.setBusName(booking1.getBusroute().getBusName());
		dto.setBusServiceNumber(booking1.getBusroute().getBusServiceNumber());
		dto.setNoOfSeats(booking1.getNoOfSeats());
		dto.setJourneyDate(booking1.getJourneyDate());
		
	}
		return dto;
		
	}
	

}
